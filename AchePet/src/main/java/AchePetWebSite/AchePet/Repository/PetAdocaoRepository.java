package AchePetWebSite.AchePet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import AchePetWebSite.AchePet.Model.PetAdocao;

import java.util.List;

public interface PetAdocaoRepository extends JpaRepository<PetAdocao, Long> {
    List<PetAdocao> findByUsuario_CdIdUsuario(Long usuarioId);
}
