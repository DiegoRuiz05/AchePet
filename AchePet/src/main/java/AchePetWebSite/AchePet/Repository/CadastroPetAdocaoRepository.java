package AchePetWebSite.AchePet.Repository;

import AchePetWebSite.AchePet.Model.CadastroPetAdocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPetAdocaoRepository extends JpaRepository<CadastroPetAdocao, Long> {
}
