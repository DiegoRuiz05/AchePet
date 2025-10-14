package AchePetWebSite.AchePet.Repository;



//import com.exemplo.login.model.Usuario;
import AchePetWebSite.AchePet.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsuario(String usuario);
}

