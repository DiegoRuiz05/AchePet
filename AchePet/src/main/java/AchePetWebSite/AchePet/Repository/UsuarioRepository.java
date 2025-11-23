package AchePetWebSite.AchePet.Repository;

import AchePetWebSite.AchePet.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar por nome de usuário
    Optional<Usuario> findByNmUsuario(String nmUsuario);

    // Buscar por e-mail
    Optional<Usuario> findByNmEmail(String nmEmail);

    // Verificar duplicidade
    boolean existsByNmUsuario(String nmUsuario);
    boolean existsByNmEmail(String nmEmail);
    boolean existsByCdCpf(String cdCpf);

    // --- Novo: buscar usuário com pets carregados (fetch join) para evitar LazyInitializationException
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.pets WHERE u.cdIdUsuario = :id")
    Optional<Usuario> buscarUsuarioComPets(@Param("id") Long id);

    @Query("""
    SELECT u FROM Usuario u
    LEFT JOIN FETCH u.pets
""")
    List<Usuario> buscarTodosComPets();


}
