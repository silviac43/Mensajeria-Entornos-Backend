package entornos.taller.controller;

import entornos.taller.model.Cliente;
import entornos.taller.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<?> listarClientes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cliente> clientes = clienteService.listarTodos();
            response.put("status", "success");
            response.put("data", clientes);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            response.put("status", "error");
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Ocurrió un error al procesar la solicitud");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarClientePorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Cliente> clienteOptional = clienteService.buscarPorId(id);

            if (clienteOptional.isPresent()) {
                response.put("status", "success");
                response.put("data", clienteOptional.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "No se encontró el cliente con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Ocurrió un error al buscar el cliente");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody Cliente cliente) {
        Map<String, Object> response = new HashMap<>();

        try {
            Cliente clienteGuardado = clienteService.guardar(cliente);
            response.put("status", "success");
            response.put("message", "Cliente creado exitosamente");
            response.put("data", clienteGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            response.put("status", "error");
            response.put("message", ex.getMessage());
            HttpStatus status = ex.getMessage().contains("Rol invalido") ||
                    ex.getMessage().contains("No puede registrar clientes")
                    ? HttpStatus.FORBIDDEN
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Error al intentar crear el cliente");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /*@PostMapping
    public Cliente crearCliente(@RequestBody Cliente cliente) {
        return clienteService.guardar(cliente);
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable Long id,
            @RequestBody Cliente clienteActualizado) {

        Map<String, Object> response = new HashMap<>();

        try {
            Cliente clienteActualizadoDB = clienteService.actualizar(id, clienteActualizado);

            response.put("status", "success");
            response.put("message", "Cliente actualizado correctamente");
            response.put("data", clienteActualizadoDB);

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            response.put("status", "error");
            response.put("message", ex.getMessage());

            HttpStatus status;
            if (ex.getMessage().contains("Rol invalido")) {
                status = HttpStatus.FORBIDDEN;
            } else if (ex.getMessage().contains("No existe un cliente")) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.BAD_REQUEST;
            }

            return ResponseEntity.status(status).body(response);

        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Error interno al actualizar el cliente");
            return ResponseEntity.internalServerError().body(response); // 500
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            clienteService.eliminar(id);
            response.put("status", "success");
            response.put("message", "Cliente eliminado correctamente");
            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            response.put("status", "error");
            response.put("message", ex.getMessage());

            HttpStatus status;
            if (ex.getMessage().contains("Rol invalido")) {
                status = HttpStatus.FORBIDDEN;
            } else if (ex.getMessage().contains("No existe")) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.BAD_REQUEST;
            }
            return ResponseEntity.status(status).body(response);

        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Error interno al eliminar el cliente");
            return ResponseEntity.internalServerError().body(response); // 500
        }
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<?> listarClientesPorEmpresa(@PathVariable Long empresaId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cliente> clientes = clienteService.listarClientesPorEmpresaId(empresaId);
            response.put("status", "success");
            response.put("data", clientes);
            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            response.put("status", "error");
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Error al procesar la solicitud");
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
