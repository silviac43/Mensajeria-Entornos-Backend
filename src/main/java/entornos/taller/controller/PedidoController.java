package entornos.taller.controller;

import entornos.taller.model.Pedido;
import entornos.taller.service.PedidoService;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosPedidos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Pedido> pedidos = pedidoService.listarTodos();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Pedidos encontrados: " + pedidos.size());
            response.put("data", pedidos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al obtener la lista de pedidos");
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Pedido> pedidoOptional = pedidoService.buscarPorId(id);

            if (pedidoOptional.isPresent()) {
                return ResponseEntity.ok(pedidoOptional.get());
            } else {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "No se encontró el pedido con ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException ex) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "ID inválido");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al buscar el pedido");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        Map<String, Object> response = new HashMap<>();

        try {
            Pedido savedPedido = pedidoService.guardar(pedido);

            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Pedido creado exitosamente");
            response.put("data", savedPedido);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            if (e.getMessage().contains("Rol invalido")) {
                status = HttpStatus.FORBIDDEN;
            } else if (e.getMessage().contains("misma empresa")) {
                status = HttpStatus.UNAUTHORIZED;
            }
            response.put("status", status.value());
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al procesar el pedido");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarPedido(
            @PathVariable Long id,
            @RequestBody Pedido pedidoActualizado) {

        Map<String, Object> response = new HashMap<>();

        try {
            Pedido updatedPedido = pedidoService.actualizar(id, pedidoActualizado);

            response.put("status", HttpStatus.OK.value());
            response.put("message", "Pedido actualizado exitosamente");
            response.put("data", updatedPedido);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            if (e.getMessage().contains("Pedido no encontrado")) {
                status = HttpStatus.NOT_FOUND;
            } else if (e.getMessage().contains("Rol invalido")) {
                status = HttpStatus.FORBIDDEN;
            } else if (e.getMessage().contains("misma empresa")) {
                status = HttpStatus.UNAUTHORIZED;
            }

            response.put("status", status.value());
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al actualizar el pedido");
            response.put("error", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarPedido(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!pedidoService.buscarPorId(id).isPresent()) {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "No se encontró el pedido con ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            pedidoService.eliminar(id);

            response.put("status", HttpStatus.OK.value());
            response.put("message", "Pedido eliminado exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            if (e.getMessage().contains("Rol invalido")) {
                status = HttpStatus.FORBIDDEN;
            } else if (e.getMessage().contains("not found")) {
                status = HttpStatus.NOT_FOUND;
            }
            response.put("status", status.value());
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al eliminar el pedido");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Map<String, Object>> listarPedidosPorMensajeria(
            @PathVariable Long empresaId) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<Pedido> pedidos = pedidoService.listarPedidosPorMensajeria(empresaId);

            response.put("status", HttpStatus.OK.value());
            response.put("message", "Pedidos encontrados: " + pedidos.size());
            response.put("data", pedidos);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;

            if (e.getMessage().contains("No hay pedidos registrados")) {
                response.put("status", status.value());
                response.put("message", e.getMessage());
                response.put("data", Collections.emptyList());

                return new ResponseEntity<>(response, status);
            }
            
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al listar los pedidos por mensajeria");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mensajero/{mensajeroId}")
    public ResponseEntity<Map<String, Object>> listarPedidosPorMensajero(
            @PathVariable Long mensajeroId) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<Pedido> pedidos = pedidoService.listarPedidosPorMensajero(mensajeroId);

            response.put("status", HttpStatus.OK.value());
            response.put("message", "Pedidos encontrados: " + pedidos.size());
            response.put("data", pedidos);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;

            if (e.getMessage().contains("No hay pedidos registrados asignados al mensajero con id: " + mensajeroId)) {
                response.put("status", status.value());
                response.put("message", e.getMessage());
                response.put("data", Collections.emptyList());

                return new ResponseEntity<>(response, status);
            }

            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al listar los pedidos por mensajero");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}