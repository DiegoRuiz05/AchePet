package AchePetWebSite.AchePet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import AchePetWebSite.AchePet.Model.PetAdocao;

import java.util.List;

@Repository
public interface PetAdocaoRepository extends JpaRepository<PetAdocao, Long> {

    // Listar pets de um usuário específico
    List<PetAdocao> findByUsuario_CdIdUsuario(Long usuarioId);

    // Listar pets por status (ATIVO / INATIVO)
    List<PetAdocao> findByDsStatus(String dsStatus);
}
