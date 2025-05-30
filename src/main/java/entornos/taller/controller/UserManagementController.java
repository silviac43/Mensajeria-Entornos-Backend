package entornos.taller.controller;

import entornos.taller.model.Rol;
import entornos.taller.model.Usuario;
import entornos.taller.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {
    private final UsuarioService usuarioService;

    @Autowired
    public UserManagementController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Crear usuario
    @PostMapping
    public ResponseEntity<Usuario> createUser(@RequestBody Usuario usuario) {
        Usuario newUser = usuarioService.registrar(usuario);
        return ResponseEntity.ok(newUser);
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsers() {
        List<Usuario> users = usuarioService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUser(
            @PathVariable Long id,
            @RequestBody Usuario nuevoUsuario) {
        Usuario updatedUser = usuarioService.updateUser(id, nuevoUsuario);
        return ResponseEntity.ok(updatedUser);
    }

    // Borrar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Usuario u = usuarioService.findById(id).orElseThrow(() -> {
            throw new RuntimeException("No existe el usuario con el id: " + id);
        });
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return (ResponseEntity<Void>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // obtener usuarios por empresa
    @GetMapping("/por-empresa/{empresaId}")
    public ResponseEntity<List<Usuario>> getUsersByEmpresa(@PathVariable Long empresaId) {
        List<Usuario> users = usuarioService.findUsersByEmpresa(empresaId);
        return ResponseEntity.ok(users);
    }

    // obtener usuarios por empresa y rol
    @GetMapping("/por-empresa-rol/{empresaId}/{rol}")
    public ResponseEntity<List<Usuario>> getUsersByEmpresaAndRol(
            @PathVariable Long empresaId,
            @PathVariable Rol rol) {
        List<Usuario> users = usuarioService.findUsersByEmpresaIdAndRol(empresaId, rol);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-rol/{rol}")
    public ResponseEntity<List<Usuario>> getUsersByRol(@PathVariable Rol rol) {
        List<Usuario> users = usuarioService.findUsersByRol(rol);
        return ResponseEntity.ok(users);
    }

    // Restablecer contraseña de usuario (simulación)
    @PutMapping("/reset-password/{id}")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id) {
        try {
            String nuevaPassword = usuarioService.resetPassword(id);
            System.out.println("Contraseña restablecida para usuario ID " + id + ": " + nuevaPassword);
            return ResponseEntity.noContent().build(); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
