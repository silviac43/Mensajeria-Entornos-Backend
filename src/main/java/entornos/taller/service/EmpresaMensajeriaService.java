package entornos.taller.service;

import entornos.taller.model.EmpresaMensajeria;
import entornos.taller.model.Usuario;
import entornos.taller.repository.EmpresaMensajeriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaMensajeriaService {

    private final EmpresaMensajeriaRepository empresaRepository;

    public EmpresaMensajeriaService(EmpresaMensajeriaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public List<EmpresaMensajeria> listarTodas() {
        List<EmpresaMensajeria> result = empresaRepository.findAll();
        if(result.isEmpty()) {
            throw new RuntimeException("No empresas de mensajeria encontradas");
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<EmpresaMensajeria> buscarPorId(Long id) {
        return empresaRepository.findById(id);

    }

    @Transactional
    public void eliminar(Long id) {
        empresaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<EmpresaMensajeria> findByUser(Usuario usuario) {
        return empresaRepository.findByUsuario(usuario);
    }

    @Transactional
    public EmpresaMensajeria guardar(EmpresaMensajeria empresa) {
        // Aquí puedes agregar lógica para validaciones extra antes de guardar si quieres
        return empresaRepository.save(empresa);
    }

    @Transactional
    public EmpresaMensajeria actualizar(Long id, EmpresaMensajeria empresaActualizada) {
        EmpresaMensajeria empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe empresa con id " + id));

        empresa.setNombre(empresaActualizada.getNombre());

        return empresaRepository.save(empresa);
    }
}
