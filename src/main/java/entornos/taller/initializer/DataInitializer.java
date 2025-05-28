package entornos.taller.initializer;

import entornos.taller.model.EmpresaMensajeria;
import entornos.taller.model.Rol;
import entornos.taller.model.Usuario;
import entornos.taller.repository.EmpresaMensajeriaRepository;
import entornos.taller.repository.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaMensajeriaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    private Long adminId = null;
    private Long operadorId = null;
    private Long mensajeroId = null;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           EmpresaMensajeriaRepository empresaRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<EmpresaMensajeria> empresaOpt = empresaRepository.findById(1L);

        if (empresaOpt.isEmpty()) {
            System.err.println("No se encontró EmpresaMensajeria con ID 1. Usuarios no creados.");
            return;
        }

        // Admin
        Optional<Usuario> adminExistente = usuarioRepository.findByNombreUsuario("adminPrueba");
        if (adminExistente.isPresent()) {
            adminId = adminExistente.get().getId();
            System.out.println("Usuario temporal adminPrueba ya existe.");
        } else {
            Usuario admin = new Usuario();
            admin.setNombreUsuario("adminPrueba");
            admin.setEmail("admin_temp@correo.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.admin_mensajeria);
            admin.setEmpresaMensajeria(empresaOpt.get());

            Usuario savedAdmin = usuarioRepository.save(admin);
            adminId = savedAdmin.getId();
            System.out.println("Usuario temporal creado: adminPrueba");
        }

        // Operador
        Optional<Usuario> operadorExistente = usuarioRepository.findByNombreUsuario("operadorPrueba");
        if (operadorExistente.isPresent()) {
            operadorId = operadorExistente.get().getId();
            System.out.println("Usuario temporal operadorPrueba ya existe.");
        } else {
            Usuario operador = new Usuario();
            operador.setNombreUsuario("operadorPrueba");
            operador.setEmail("operador_temp@correo.com");
            operador.setPassword(passwordEncoder.encode("operador123"));
            operador.setRol(Rol.operador);
            operador.setEmpresaMensajeria(empresaOpt.get());

            Usuario savedOperador = usuarioRepository.save(operador);
            operadorId = savedOperador.getId();
            System.out.println("Usuario temporal creado: operadorPrueba");
        }

        // Mensajero
        Optional<Usuario> mensajeroExistente = usuarioRepository.findByNombreUsuario("mensajeroPrueba");
        if (mensajeroExistente.isPresent()) {
            mensajeroId = mensajeroExistente.get().getId();
            System.out.println("Usuario temporal mensajeroPrueba ya existe.");
        } else {
            Usuario mensajero = new Usuario();
            mensajero.setNombreUsuario("mensajeroPrueba");
            mensajero.setEmail("mensajero_temp@correo.com");
            mensajero.setPassword(passwordEncoder.encode("mensajero123"));
            mensajero.setRol(Rol.mensajero);
            mensajero.setEmpresaMensajeria(empresaOpt.get());

            Usuario savedMensajero = usuarioRepository.save(mensajero);
            mensajeroId = savedMensajero.getId();
            System.out.println("Usuario temporal creado: mensajeroPrueba");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (adminId != null) usuarioRepository.deleteById(adminId);
                if (operadorId != null) usuarioRepository.deleteById(operadorId);
                if (mensajeroId != null) usuarioRepository.deleteById(mensajeroId);
                System.out.println("Usuarios temporales eliminados al cerrar la app.");
            } catch (Exception e) {
                System.err.println("Error al eliminar usuarios temporales: " + e.getMessage());
            }
        }));
    }
}
