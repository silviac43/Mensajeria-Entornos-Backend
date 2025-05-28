package entornos.taller.repository;

import entornos.taller.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /*"SELECT * FROM cliente WHERE mensajeria_id = ?1"*/
    List<Cliente> findByEmpresaMensajeriaId(Long empresaMensajeriaId);
}
