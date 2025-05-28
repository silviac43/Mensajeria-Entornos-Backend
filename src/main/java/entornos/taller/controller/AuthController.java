package entornos.taller.controller;

import entornos.taller.model.Usuario;
import entornos.taller.security.JwtUtil;
import entornos.taller.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        String nombreUsuario = credentials.get("nombreUsuario");
        String password = credentials.get("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nombreUsuario, password));

        UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);
        String token = jwtUtil.generateToken(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = authService.findByNombreUsuario(userDetails.getUsername());

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getId());
        response.put("email", usuario.getEmail());
        response.put("nombreUsuario", usuario.getNombreUsuario());
        response.put("rol", usuario.getRol());
        response.put("fechaCreacion", usuario.getFechaCreacion());
  
        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("id", usuario.getEmpresaMensajeria().getId());
        empresaData.put("nombre", usuario.getEmpresaMensajeria().getNombre());
        response.put("empresaMensajeria", empresaData);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(Authentication authentication, @RequestBody Usuario datosActualizados) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario actual = authService.findByNombreUsuario(userDetails.getUsername());

        if (actual == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        if (!actual.getNombreUsuario().equals(datosActualizados.getNombreUsuario())) {
            if (authService.existsByNombreUsuario(datosActualizados.getNombreUsuario())) {
                return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
            }
            actual.setNombreUsuario(datosActualizados.getNombreUsuario());
        }

        if (!actual.getEmail().equals(datosActualizados.getEmail())) {
            actual.setEmail(datosActualizados.getEmail());
        }

        if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isBlank()) {
            actual.setPassword(authService.encodePassword(datosActualizados.getPassword()));
        }

        authService.save(actual);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

}