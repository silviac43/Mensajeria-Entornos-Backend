package entornos.taller.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entornos.taller.model.Mensajero;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeroRepository extends JpaRepository<Mensajero, Long> {
    /*"SELECT m FROM Mensajero m WHERE m.nombreUsuario = :nombreUsuario"*/
    Optional<Mensajero> findByNombreUsuario(String nombreUsuario);

    /*"SELECT m FROM Mensajero m WHERE m.empresaMensajeria.id=:empresaMensajeriaId AND m.disponibilidad =:disponibilidad"*/
    List<Mensajero> findByEmpresaMensajeriaIdAndDisponibilidad(Long empresaMensajeriaId, Boolean disponibilidad);

    /*"SELECT m FROM Mensajero m WHERE m.empresaMensajeria.id = :empresaMensajeriaId"*/
    List<Mensajero> findByEmpresaMensajeriaId(Long empresaMensajeriaId);
}