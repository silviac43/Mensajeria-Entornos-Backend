package entornos.taller.controller;

import entornos.taller.model.Mensajero;
import entornos.taller.service.MensajeroService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/mensajeros")
public class MensajeroController {

    private final MensajeroService service;

    public MensajeroController(MensajeroService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosMensajeros() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Mensajero> mensajeros = service.findAll();
            return ResponseEntity.ok(mensajeros);
        } catch (RuntimeException ex) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al obtener la lista de mensajeros");
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMensajeroPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Mensajero> mensajeroOptional = service.findById(id);

            if (mensajeroOptional.isPresent()) {
                return ResponseEntity.ok(mensajeroOptional.get());
            } else {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "No se encontró el mensajero con ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException ex) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "ID inválido");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al buscar el mensajero");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> registrarMensajero(@RequestBody Mensajero mensajero) {
        Map<String, Object> response = new HashMap<>();
        try {
            Mensajero mensajeroRegistrado = service.save(mensajero);
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Mensajero registrado exitosamente");
            response.put("data", mensajeroRegistrado);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            HttpStatus status = ex.getMessage().contains("Rol invalido") ?
                    HttpStatus.FORBIDDEN : HttpStatus.BAD_REQUEST;
            response.put("status", status.value());
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, status);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al registrar el mensajero");
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mensajero> update(@PathVariable Long id, @RequestBody Mensajero mensajero) {
        return service.findById(id).map(existing -> {
            mensajero.setId(existing.getId());
            return ResponseEntity.ok(service.save(mensajero));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/por-usuario/{nombreUsuario}")
    public ResponseEntity<?> buscarMensajeroPorUsername(
            @PathVariable @NotBlank @Size(min = 3, max = 20) String nombreUsuario) {

        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Mensajero> mensajeroOptional = service.findByUsername(nombreUsuario);

            if (mensajeroOptional.isPresent()) {
                response.put("status", HttpStatus.OK.value());
                response.put("data", mensajeroOptional.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "No se encontró mensajero con nombre de usuario: " + nombreUsuario);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al buscar mensajero");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMensajero(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            service.deleteById(id);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Mensajero eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            response.put("status", HttpStatus.FORBIDDEN.value());
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }  catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al eliminar el mensajero");
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<?> obtenerMensajerosPorEmpresa(@PathVariable Long empresaId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Mensajero> mensajeros = service.findAllByEmpresaMensajeriaId(empresaId);
            response.put("status", HttpStatus.OK.value());
            response.put("data", mensajeros);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al obtener mensajeros por empresa");
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
