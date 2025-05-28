package entornos.taller.service;

import entornos.taller.model.*;
import entornos.taller.repository.EmpresaMensajeriaRepository;
import entornos.taller.repository.ClienteRepository;
import entornos.taller.repository.MensajeroRepository;
import entornos.taller.repository.PedidoRepository;
import entornos.taller.security.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final HistorialPedidoService historialPedidoService;
    private final SecurityUtils securityUtils;
    private record ChangeRecord(String oldValue, String newValue) {}

    @Autowired
    private EmpresaMensajeriaRepository empresaMensajeriaRepository;
    @Autowired
    private MensajeroRepository mensajeroRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public PedidoService(PedidoRepository pedidoRepository, MensajeroService mensajeroService, HistorialPedidoService historialPedidoService, SecurityUtils securityUtils) {
        this.pedidoRepository = pedidoRepository;
        this.historialPedidoService = historialPedidoService;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        List<Pedido> result = pedidoRepository.findAll();
        if (result.isEmpty()) {
            throw new RuntimeException("No hay pedidos registrados");
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido guardar(Pedido pedido) {
            Usuario u = securityUtils.getCurrentUser();
    if (u.getRol() == Rol.mensajero) {
        throw new RuntimeException("Rol invalido para registrar un pedido");
    } else if (u.getRol() == Rol.operador && 
               !Objects.equals(u.getEmpresaMensajeria().getId(), pedido.getEmpresaMensajeria().getId())) {
        throw new RuntimeException("No puede registrar un pedido que no es de su misma empresa.");
    }
        Pedido savedPedido = pedidoRepository.save(pedido);
        HistorialPedido historialPedido = new HistorialPedido(
                savedPedido,
                TipoCambio.ESTADO,
                pedido.getEstado() != null ? pedido.getEstado().name() : "null",
                EstadoPedido.ASIGNADO.name(),
                u
        );
        historialPedidoService.save(historialPedido);
        return savedPedido;
    }

    private String formatValue(Object val) {
        if (val == null) return "null";
        return val.toString();
    }

    private void trackChange(
        Map<TipoCambio, ChangeRecord> changes,
        TipoCambio changeType,
        Object oldVal,
        Object newVal
    ) {
        
        if (!Objects.equals(oldVal, newVal)) {
            changes.put(
                changeType,
                new ChangeRecord(
                    formatValue(oldVal),
                    formatValue(newVal)
                )
            );
        }
    }

    @Transactional
    public Pedido actualizar(Long id, Pedido pedidoActualizado) {
        Usuario u = securityUtils.getCurrentUser();
        Pedido pedidoToUpdate = buscarPorId(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (u.getRol() == Rol.mensajero) {
            throw new RuntimeException("Rol invalido para registrar un pedido");
        } else if (u.getRol() == Rol.operador && 
                !Objects.equals(u.getEmpresaMensajeria().getId(), pedidoToUpdate.getEmpresaMensajeria().getId())) {
            throw new RuntimeException("No puede registrar un pedido que no es de su misma empresa.");
        }

        if (pedidoActualizado.getCliente() != null && pedidoActualizado.getCliente().getId() != null) {
            pedidoActualizado.setCliente(clienteRepository.findById(pedidoActualizado.getCliente().getId()).orElse(null));
        }

        if (pedidoActualizado.getMensajero() != null && pedidoActualizado.getMensajero().getId() != null) {
            pedidoActualizado.setMensajero(mensajeroRepository.findById(pedidoActualizado.getMensajero().getId()).orElse(null));
        }

        if (pedidoActualizado.getEmpresaMensajeria() != null && pedidoActualizado.getEmpresaMensajeria().getId() != null) {
            pedidoActualizado.setEmpresaMensajeria(empresaMensajeriaRepository.findById(pedidoActualizado.getEmpresaMensajeria().getId()).orElse(null));
        }

        Map<TipoCambio, ChangeRecord> changes = new EnumMap<>(TipoCambio.class);

        trackChange(changes, TipoCambio.ESTADO,
            pedidoToUpdate.getEstado(), pedidoActualizado.getEstado());

        trackChange(changes, TipoCambio.CLIENTE,
            pedidoToUpdate.getCliente(), pedidoActualizado.getCliente());

        trackChange(changes, TipoCambio.MENSAJERO, 
            pedidoToUpdate.getMensajero(), pedidoActualizado.getMensajero());

        trackChange(changes, TipoCambio.EMPRESA_MENSAJERIA,
            pedidoToUpdate.getEmpresaMensajeria(), pedidoActualizado.getEmpresaMensajeria());

        trackChange(changes, TipoCambio.DIRECCION_RECOGIDA,
            pedidoToUpdate.getDireccionRecogida(), pedidoActualizado.getDireccionRecogida());

        trackChange(changes, TipoCambio.DIRECCION_ENTREGA,
            pedidoToUpdate.getDireccionEntrega(), pedidoActualizado.getDireccionEntrega());

        trackChange(changes, TipoCambio.TELEFONO_RECOGIDA,
            pedidoToUpdate.getTelefonoRecogida(), pedidoActualizado.getTelefonoRecogida());

        trackChange(changes, TipoCambio.TELEFONO_ENTREGA,
            pedidoToUpdate.getTelefonoEntrega(), pedidoActualizado.getTelefonoEntrega());

        trackChange(changes, TipoCambio.TIPO_PAQUETE,
            pedidoToUpdate.getTipoPaquete(), pedidoActualizado.getTipoPaquete());

        trackChange(changes, TipoCambio.NOTAS,
            pedidoToUpdate.getNotas(), pedidoActualizado.getNotas());

        pedidoToUpdate.setEstado(pedidoActualizado.getEstado());
        pedidoToUpdate.setCliente(pedidoActualizado.getCliente());
        pedidoToUpdate.setMensajero(pedidoActualizado.getMensajero());
        pedidoToUpdate.setEmpresaMensajeria(pedidoActualizado.getEmpresaMensajeria());
        pedidoToUpdate.setDireccionRecogida(pedidoActualizado.getDireccionRecogida());
        pedidoToUpdate.setDireccionEntrega(pedidoActualizado.getDireccionEntrega());
        pedidoToUpdate.setTelefonoRecogida(pedidoActualizado.getTelefonoRecogida());
        pedidoToUpdate.setTelefonoEntrega(pedidoActualizado.getTelefonoEntrega());
        pedidoToUpdate.setTipoPaquete(pedidoActualizado.getTipoPaquete());
        pedidoToUpdate.setNotas(pedidoActualizado.getNotas());

        Pedido updatedPedido = pedidoRepository.save(pedidoToUpdate);

        changes.forEach((tipoCambio, change) ->
            historialPedidoService.save(new HistorialPedido(
                updatedPedido,
                tipoCambio,
                change.oldValue(),
                change.newValue(),
                u
            ))
        );

        return updatedPedido;
    }


    @Transactional
    public void eliminar(Long id) {
        Usuario u = securityUtils.getCurrentUser();
        if (u.getRol() == Rol.mensajero) {
            throw new RuntimeException("Rol invalido para eliminar un pedido.");
        }
        pedidoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosPorMensajero(Long mensajeroId) {
        List<Pedido> result = pedidoRepository.findByMensajeroId(mensajeroId);
        if (result.isEmpty()) {
            throw new RuntimeException("No hay pedidos registrados para el mensajero con id "+mensajeroId);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosPorCliente(Long clienteId) {
        List<Pedido> result = pedidoRepository.findByClienteId(clienteId);
        if (result.isEmpty()) {
            throw new RuntimeException("No hay pedidos registrados para el cliente con id "+clienteId);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosPorMensajeria(Long empresaMensajeriaId) {
        List<Pedido> result = pedidoRepository.findByEmpresaMensajeriaId(empresaMensajeriaId);
        if (result.isEmpty()) {
            throw new RuntimeException("No hay pedidos registrados para el cliente con id "+empresaMensajeriaId);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosPorEstado(EstadoPedido estado) {
        List<Pedido> result = pedidoRepository.findByEstado(estado);
        if (result.isEmpty()) {
            throw new RuntimeException("No hay pedidos registrados con el estado "+ estado.name());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosPorFecha(Instant fecha) {
        List<Pedido> result = pedidoRepository.findByFechaCreacionDate(fecha);
        if (result.isEmpty()) {
            throw new RuntimeException("No hay pedidos registrados en la fecha "+ fecha.toString());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosPorRangoFechas(Instant start, Instant end) {
        List<Pedido> result = pedidoRepository.findByFechaCreacionBetween(start, end);
        if (result.isEmpty()) {
            throw new RuntimeException("No hay pedidos registrados entre "+ start.toString() + " - " + end.toString());
        }
        return result;
    }
}
