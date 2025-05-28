package entornos.taller.security;

import entornos.taller.model.Rol;
import entornos.taller.model.Usuario;
import entornos.taller.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    private final UsuarioService usuarioService;

    public SecurityUtils(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene el current user
     * @throws RuntimeException if no se encuentra usuario o no esta autenticado.
     */
    public Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }

    /**
     * Valida si el current user tiene un determinado rol
     */
    public boolean currentUserHasRole(Rol rol) {
        return getCurrentUser().getRol() == rol;
    }
}
