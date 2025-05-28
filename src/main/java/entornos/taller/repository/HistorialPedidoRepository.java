package entornos.taller.repository;

import entornos.taller.model.HistorialPedido;

import java.util.List;

import entornos.taller.model.TipoCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialPedidoRepository extends JpaRepository<HistorialPedido, Long> {
    @Query("SELECT h FROM HistorialPedido h WHERE h.pedido.empresaMensajeria.id = :mensajeriaId")
    List<HistorialPedido> findByEmpresaMensajeriaId(@Param("mensajeriaId") Long mensajeriaId);

    List<HistorialPedido> findByUsuarioId(Long usuarioId);

    List<HistorialPedido> findByPedidoId(Long pedidoId);

    List<HistorialPedido> findByPedidoIdAndTipoCambio(Long pedidoId, TipoCambio tipoCambio);

    @Query("SELECT h FROM HistorialPedido h JOIN Pedido p on h.pedido = p WHERE p.mensajero.id = :mensajeroId")
    List<HistorialPedido> findByMensajeroId(@Param("mensajeroId") Long mensajeroId);
}
