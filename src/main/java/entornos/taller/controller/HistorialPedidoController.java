package entornos.taller.controller;

import entornos.taller.model.*;
import entornos.taller.service.HistorialPedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import entornos.taller.service.UsuarioService;

@RestController
@RequestMapping("/api/historialpedido")
public class HistorialPedidoController {

    private final HistorialPedidoService historialPedidoService;
    
    @Autowired
    public HistorialPedidoController(HistorialPedidoService historialPedidoService, UsuarioService usuarioService) {
        this.historialPedidoService = historialPedidoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<HistorialPedido> historialPedidoOptional = historialPedidoService.findById(id);

            if (historialPedidoOptional.isPresent()) {
                return ResponseEntity.ok(historialPedidoOptional.get());
            } else {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "No se encontró el historial pedido con ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException ex) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "ID inválido");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al buscar el historial de pedido");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<?> obtenerByEmpresaId(@PathVariable Long empresaId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<HistorialPedido> historialPedidos = historialPedidoService.findByEmpresaMensajeriaId(empresaId);
            response.put("status", "success");
            response.put("data", historialPedidos);
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

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<HistorialPedido> historialPedidos = historialPedidoService.findAll();
            response.put("status", "success");
            response.put("data", historialPedidos);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            response.put("status", "error");
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Error al obtener el historial");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/mensajero/{mensajeroId}")
    public ResponseEntity<?> obtenerHistorialPorMensajero(@PathVariable Long mensajeroId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<HistorialPedido> historial = historialPedidoService.findByMensajeroId(mensajeroId);
            response.put("status", "success");
            response.put("data", historial);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al obtener historial de pedidos para el mensajero");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}