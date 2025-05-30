package entornos.taller.service;

import entornos.taller.controller.AuthController;
import entornos.taller.model.Rol;
import entornos.taller.model.Usuario;
import entornos.taller.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;


@Service
public class UsuarioService implements UserDetailsService { 

    private final UsuarioRepository usuarioRepository;
    /*private final SecurityUtils securityUtils;*/
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario registrar(Usuario usuario) {
        String passwordSinHashear = generarPasswordRandom(12);
        usuario.setPassword(passwordEncoder.encode(passwordSinHashear));
        Usuario savedUser = usuarioRepository.save(usuario);
        System.out.println("Enviando email a " + usuario.getEmail() +
            " con la contraseña temporal: " + passwordSinHashear);

        return savedUser;
    }

    private String generarPasswordRandom(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+";
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = (int) (Math.random() * chars.length());
            pass.append(chars.charAt(idx));
        }
        return pass.toString();
    }

    @Transactional
    public String resetPassword(Long id) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        
        String nuevaPassword = generarPasswordRandom(12);
        user.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(user);

        System.out.println("Restablecida contraseña de " + user.getEmail() +
            " con nueva contraseña temporal: " + nuevaPassword);

        return nuevaPassword; 
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAllUsers() {
        List<Usuario> result = usuarioRepository.findAll();
        if (result.isEmpty()) {
            throw new RuntimeException("No se encontraron usuarios registrados.");
        }
        return result;
    }

    public boolean validarPassword(String password, String hash) {
        return passwordEncoder.matches(password, hash);
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));
    
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getNombreUsuario())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name()) 
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> findUsersByEmpresa(Long empresaMensajeriaId) {
        List<Usuario> result = usuarioRepository.findByEmpresaMensajeriaId(empresaMensajeriaId);
        if (result.isEmpty()) {
            throw new RuntimeException("No se encontraron usuarios asociados a la empresa de mensajeria con ID: " + empresaMensajeriaId);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Usuario> findUsersByEmpresaIdAndRol(Long empresaMensajeriaId, Rol rol) {
        List<Usuario> result = usuarioRepository.findByEmpresaMensajeriaIdAndRol(empresaMensajeriaId, rol);
        if (result.isEmpty()) {
            throw new RuntimeException("No se encontraron usuarios con el rol " + rol + " asociados a la empresa de mensajeria con ID: " + empresaMensajeriaId);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Usuario> findUsersByRol(Rol rol) {
        List<Usuario> result = usuarioRepository.findByRol(rol);
        if (result.isEmpty()) {
            throw new RuntimeException("No se encontraron usuarios con el rol " + rol);
        }
        return result;
    }

    @Transactional
    public void deleteById(Long id) {
        Usuario usuarioToDelete = findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario u = findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        if (u.getRol() != Rol.admin_mensajeria) {
            throw new RuntimeException("Rol invalido para eliminar un usuario. Solo administradores pueden.");
        } else if (usuarioToDelete.getNombreUsuario().equals(u.getNombreUsuario())) {
            throw new RuntimeException("No puedes eliminarte a ti mismo");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public Usuario updateUser(Long id, Usuario nuevoUsuario) {
        Usuario existingUser = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        existingUser.setNombreUsuario(nuevoUsuario.getNombreUsuario());
        existingUser.setEmail(nuevoUsuario.getEmail());

        // Solo actualizar contraseña si viene y no está vacía
        if (nuevoUsuario.getPassword() != null && !nuevoUsuario.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        }
        // Si no viene contraseña, no actualizar ni tirar error

        if (nuevoUsuario.getEmpresaMensajeria() != null) {
            existingUser.setEmpresaMensajeria(nuevoUsuario.getEmpresaMensajeria());
        }

        existingUser.setRol(nuevoUsuario.getRol());

        return usuarioRepository.save(existingUser);
    }


}
