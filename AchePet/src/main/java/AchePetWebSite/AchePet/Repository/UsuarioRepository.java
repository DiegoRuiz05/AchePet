package AchePetWebSite.AchePet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import AchePetWebSite.AchePet.Model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNmUsuario(String nmUsuario);
    boolean existsByNmUsuario(String nmUsuario);
    boolean existsByNmEmail(String email);
    boolean existsByCdCpf(String cpf);
}
