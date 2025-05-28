package entornos.taller.repository;

import entornos.taller.model.EstadoPedido;
import entornos.taller.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /*"SELECT p.* FROM pedido p WHERE p.usuario_mensajero_id = ?1"*/
    List<Pedido> findByMensajeroId(Long mensajeroId);

    /*"SELECT p.* FROM pedido p WHERE p.cliente_id = ?1"*/
    /*SI se le pasa null, entonces devolvera todos los pedidos que no tienen cliente asignado*/
    /*Es decir, los pedidos que tienen datos de clientes custom*/
    List<Pedido> findByClienteId(Long clienteId);

    /*"SELECT * FROM pedido WHERE mensajeria_id = ?1"*/
    List<Pedido> findByEmpresaMensajeriaId(Long empresaMensajeriaId);

    /*"SELECT * FROM pedido WHERE estado = ?1;"*/
    List<Pedido> findByEstado(EstadoPedido estado);

    // Encontrar pedidos creados un dia especifico (ignorando hora)
    @Query("SELECT p FROM Pedido p WHERE DATE(p.fechaCreacion) = DATE(:fecha)")
    List<Pedido> findByFechaCreacionDate(@Param("fecha") Instant fecha);

    // Encontrar pedidos entre dos timestamps
    List<Pedido> findByFechaCreacionBetween(Instant start, Instant end);
}
