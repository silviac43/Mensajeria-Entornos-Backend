package entornos.taller.service;

import entornos.taller.model.HistorialPedido;
import entornos.taller.model.Rol;
import entornos.taller.model.TipoCambio;
import entornos.taller.model.Usuario;
import entornos.taller.repository.HistorialPedidoRepository;
import entornos.taller.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class HistorialPedidoService {
    private final HistorialPedidoRepository historialPedidoRepository;
    private final SecurityUtils securityUtils;

    public HistorialPedidoService(HistorialPedidoRepository historialPedidoRepository, SecurityUtils securityUtils) {
        this.historialPedidoRepository = historialPedidoRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public List<HistorialPedido> findAll() {
        List<HistorialPedido> result = historialPedidoRepository.findAll();
        if (result.isEmpty()) {
            throw new RuntimeException("No historial pedidos encontrados");
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<HistorialPedido> findById(Long id) {
        return historialPedidoRepository.findById(id);
    }

    @Transactional
    public HistorialPedido save(HistorialPedido historialPedido) {
        Usuario u = securityUtils.getCurrentUser();
        
        if (u.getRol() == Rol.mensajero) {
            throw new RuntimeException("Rol invalido para registrar un historial pedido");
        } else if (u.getRol() != Rol.admin_mensajeria &&
                !Objects.equals(u.getEmpresaMensajeria().getId(), historialPedido.getPedido().getEmpresaMensajeria().getId())) {
            throw new RuntimeException("No puede registrar historial de un pedido que no es de su misma empresa.");
        }

        return historialPedidoRepository.save(historialPedido);
    }


    @Transactional
    public void deleteById(Long id) {
        Usuario u = securityUtils.getCurrentUser();
        if (u.getRol() == Rol.mensajero || u.getRol() == Rol.operador) {
            throw new RuntimeException("Rol invalido para eliminar un historial pedido. Solo administradores pueden.");
        }
        historialPedidoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<HistorialPedido> findByEmpresaMensajeriaId(Long empresaId) {
        List<HistorialPedido> result = historialPedidoRepository.findByEmpresaMensajeriaId(empresaId);
        if (result.isEmpty()) {
            throw new RuntimeException("No historial pedidos encontrados");
        }
        return result;
    }

    // Busca historiales de pedido relacionados con x mensajero
    @Transactional(readOnly = true)
    public List<HistorialPedido> findByMensajeroId(Long mensajeroId) {
        List<HistorialPedido> result = historialPedidoRepository.findByMensajeroId(mensajeroId);
        if (result.isEmpty()) {
            throw new RuntimeException("No se han encontrado historial de pedidos relacionados con el mensajero id -> " + mensajeroId);
        }
        return result;
    }

    // Busca historiales de pedido por el usuario que hizo el cambio
    @Transactional(readOnly = true)
    public List<HistorialPedido> findByUsuarioId(Long usuarioId) {
        List<HistorialPedido> result = historialPedidoRepository.findByUsuarioId(usuarioId);
        if (result.isEmpty()) {
            throw new RuntimeException("No se han encontrado historial de pedidos que haya registrado el usuario con id -> " + usuarioId);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<HistorialPedido> findByPedidoId(Long pedidoId) {
        List<HistorialPedido> result = historialPedidoRepository.findByPedidoId(pedidoId);
        if (result.isEmpty()) {
            throw new RuntimeException("No historial pedidos encontrados");
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<HistorialPedido> findByPedidoIdAndTipoCambio(Long pedidoId, TipoCambio tipoCambio) {
        List<HistorialPedido> result = historialPedidoRepository.findByPedidoIdAndTipoCambio(pedidoId, tipoCambio);
        if (result.isEmpty()) {
            throw new RuntimeException("No historial pedidos encontrados");
        }
        return result;
    }
}
