package entornos.taller.repository;

import entornos.taller.model.EmpresaMensajeria;
import entornos.taller.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaMensajeriaRepository extends JpaRepository<EmpresaMensajeria, Long> {
    // encontrar empresa por uno de sus administradores
    /*@Query("SELECT e FROM EmpresaMensajeria e JOIN Usuario u ON e.id = u.empresaMensajeria.id " +
            "WHERE u = :adminUser AND u.rol = 'admin_mensajeria'")
    Optional<EmpresaMensajeria> findByAdminUser(@Param("adminUser") Usuario adminUser);*/

    // encontrar empresa por usuario
    @Query("SELECT e FROM EmpresaMensajeria e JOIN Usuario u ON e.id = u.empresaMensajeria.id WHERE u = :usuario")
    Optional<EmpresaMensajeria> findByUsuario(Usuario usuario);

}
