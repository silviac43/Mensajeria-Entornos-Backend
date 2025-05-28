package entornos.taller.controller;

import entornos.taller.model.EmpresaMensajeria;
import entornos.taller.model.Usuario;
import entornos.taller.service.EmpresaMensajeriaService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaMensajeriaController {

    private final EmpresaMensajeriaService empresaMensajeriaService;

    public EmpresaMensajeriaController(EmpresaMensajeriaService empresaService) {
        this.empresaMensajeriaService = empresaService;
    }

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<EmpresaMensajeria> empresas = empresaMensajeriaService.listarTodas();
            
            // Return in the expected format with status and data
            response.put("status", "success");
            response.put("message", "Empresas obtenidas correctamente");
            response.put("data", empresas);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            response.put("status", "error");
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Error interno al procesar la solicitud");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<EmpresaMensajeria> empresaOptional = empresaMensajeriaService.buscarPorId(id);

            if (empresaOptional.isPresent()) {
                response.put("status", "success");
                response.put("message", "Empresa encontrada");
                response.put("data", empresaOptional.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "No se encontró la empresa de mensajería con ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Error interno al buscar la empresa de mensajería");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            empresaMensajeriaService.eliminar(id);
            response.put("status", "success");
            response.put("message", "Empresa eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Error al eliminar empresa");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/por-usuario")
    public ResponseEntity<?> buscarPorUsuario(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<EmpresaMensajeria> empresaOptional = empresaMensajeriaService.findByUser(usuario);

            if (empresaOptional.isPresent()) {
                response.put("status", "success");
                response.put("message", "Empresa encontrada");
                response.put("data", empresaOptional.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "No se encontró empresa de mensajería asociada al usuario");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("message", "Error al buscar empresa por usuario");
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
public ResponseEntity<?> crearEmpresa(@Valid @RequestBody EmpresaMensajeria empresa) {
    Map<String, Object> response = new HashMap<>();
    try {
        EmpresaMensajeria empresaCreada = empresaMensajeriaService.guardar(empresa);
        response.put("status", "success");
        response.put("message", "Empresa creada correctamente");
        response.put("data", empresaCreada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception ex) {
        response.put("status", "error");
        response.put("message", "Error al crear la empresa: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

@PutMapping("/{id}")
public ResponseEntity<?> actualizarEmpresa(@PathVariable Long id, @Valid @RequestBody EmpresaMensajeria empresa) {
    Map<String, Object> response = new HashMap<>();
    try {
        EmpresaMensajeria empresaActualizada = empresaMensajeriaService.actualizar(id, empresa);
        response.put("status", "success");
        response.put("message", "Empresa actualizada correctamente");
        response.put("data", empresaActualizada);
        return ResponseEntity.ok(response);
    } catch (RuntimeException ex) {
        response.put("status", "error");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch (Exception ex) {
        response.put("status", "error");
        response.put("message", "Error al actualizar la empresa");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

}