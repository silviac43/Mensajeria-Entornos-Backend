package entornos.taller.service;

import entornos.taller.model.*;
import entornos.taller.repository.ClienteRepository;
import entornos.taller.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteService {
    /*Constructor injection*/
    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;
    private final EmpresaMensajeriaService empresaService;
    private final SecurityUtils securityUtils;

    public ClienteService(ClienteRepository clienteRepository, UsuarioService usuarioService, EmpresaMensajeriaService empresaService, SecurityUtils securityUtils) {
        this.clienteRepository = clienteRepository;
        this.usuarioService = usuarioService;
        this.empresaService = empresaService;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        List<Cliente> result = clienteRepository.findAll();
        if (result.isEmpty()) {
            throw new RuntimeException("No hay clientes registrados");
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    public Cliente guardar(Cliente cliente) {
        Usuario u = securityUtils.getCurrentUser();
        if (u.getRol() == Rol.mensajero) {
            throw new RuntimeException("Rol invalido para crear un cliente. Solo administradores y operadores pueden.");
        } else if (u.getRol() == Rol.operador && !Objects.equals(u.getEmpresaMensajeria().getId(), cliente.getEmpresaMensajeria().getId())) {
            throw new RuntimeException("No puede registrar clientes en otra empresa.");
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Usuario u = securityUtils.getCurrentUser();
        if (u.getRol() != Rol.admin_mensajeria && u.getRol() != Rol.operador) {
            throw new RuntimeException("Rol invalido para actalizar informaion de un cliente. Solo administradores y operadores pueden.");
        }
        Cliente cliente = buscarPorId(id).orElseThrow(() -> new RuntimeException("No existe un cliente con id: " + id));
        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setEmpresaMensajeria(clienteActualizado.getEmpresaMensajeria());
        cliente.setTelefonoEntrega(clienteActualizado.getTelefonoEntrega());
        cliente.setTelefonoRecogida(clienteActualizado.getTelefonoRecogida());
        cliente.setDireccionEntrega(clienteActualizado.getDireccionEntrega());
        cliente.setEmail(clienteActualizado.getEmail());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario u = securityUtils.getCurrentUser();
        if (u.getRol() != Rol.admin_mensajeria && u.getRol() != Rol.operador) {
            throw new RuntimeException("Rol invalido para eliminar un cliente. Solo administradores y operadores pueden.");
        }
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientesPorEmpresaId(Long empresaId) {
        Usuario u = securityUtils.getCurrentUser();
        if (u.getRol() != Rol.admin_mensajeria && u.getRol() != Rol.operador) {
            throw new AccessDeniedException("No tiene permiso para listar los clientes");
        }

        List<Cliente> result = clienteRepository.findByEmpresaMensajeriaId(empresaId);
        if (result.isEmpty()) {
            throw new RuntimeException("No hay clientes registrados en la empresa " + empresaId);
        }
        return result;
    }
}
