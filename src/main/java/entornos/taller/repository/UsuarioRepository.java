package entornos.taller.repository;

import entornos.taller.model.Rol;
import entornos.taller.model.Usuario;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /*"SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario"*/
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    /*"SELECT * FROM usuario WHERE mensajeria_id = ?1"*/
    List<Usuario> findByEmpresaMensajeriaId(Long empresaMensajeriaId);

    /*"SELECT * FROM usuario WHERE mensajeria_id = ?1 AND rol = ?2"*/
    List<Usuario> findByEmpresaMensajeriaIdAndRol(Long empresaMensajeriaId, Rol rol);

    /*"SELECT u FROM Usuario u WHERE u.rol = ?1"*/
    List<Usuario> findByRol(Rol rol);

}