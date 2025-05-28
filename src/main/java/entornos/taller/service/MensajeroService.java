package entornos.taller.service;

import entornos.taller.model.Mensajero;
import entornos.taller.model.Rol;
import entornos.taller.model.Usuario;
import entornos.taller.repository.MensajeroRepository;
import entornos.taller.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MensajeroService {
    private final MensajeroRepository mensajeroRepository;
    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;

    public MensajeroService(MensajeroRepository repo, UsuarioService usuarioService, SecurityUtils securityUtils) {
        this.mensajeroRepository = repo;
        this.usuarioService = usuarioService;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public List<Mensajero> findAll() {
        List<Mensajero> result = mensajeroRepository.findAll();
        if (result.isEmpty()) {
            throw new RuntimeException("No hay mensajeros registrados");
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<Mensajero> findById(Long id) {
        return mensajeroRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        Usuario u = securityUtils.getCurrentUser();
        if (u.getRol() != Rol.admin_mensajeria) {
            throw new RuntimeException("Rol invalido para eliminar un mensajero");
        }
        mensajeroRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Mensajero> findByUsername(String nombreUsuario) {
        return mensajeroRepository.findByNombreUsuario(nombreUsuario);
    }

    // Solo admin_mensajeria puede registrar mensajeros
    @Transactional
    public Mensajero save(Mensajero mensajero) {
        Usuario u = securityUtils.getCurrentUser();
        if (u.getRol() != Rol.admin_mensajeria) {
            throw new RuntimeException("Rol invalido para registrar un mensajero");
        } else if (!Objects.equals(u.getEmpresaMensajeria().getId(), mensajero.getEmpresaMensajeria().getId())) {
            throw new RuntimeException("El usuario que esta intentando registrar el pedido no es de la misma empresa del mensajero que esta intentando registrar.");
        }
        return mensajeroRepository.save(mensajero);
    }

    @Transactional(readOnly = true)
    public List<Mensajero> findAllByEmpresaMensajeriaId(Long empresaMensajeriaId) {
        List<Mensajero> result = mensajeroRepository.findByEmpresaMensajeriaId(empresaMensajeriaId);
        if (result.isEmpty()) {
            throw new RuntimeException("No hay mensajeros registrados en la empresa id: " + empresaMensajeriaId);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Mensajero> findAllByEmpresaMensajeriaIdAndDisponibilidad(Long empresaMensajeriaId, Boolean disponibilidad) {
        List<Mensajero> result = mensajeroRepository.findByEmpresaMensajeriaIdAndDisponibilidad(empresaMensajeriaId, disponibilidad);
        if (result.isEmpty()) {
            throw new RuntimeException("No hay mensajeros disponibles en la empresa id: " + empresaMensajeriaId);
        }
        return result;
    }
}
