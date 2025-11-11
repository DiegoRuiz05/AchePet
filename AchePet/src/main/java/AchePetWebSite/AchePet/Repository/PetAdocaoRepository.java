package AchePetWebSite.AchePet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import AchePetWebSite.AchePet.Model.PetAdocao;

public interface PetAdocaoRepository extends JpaRepository<PetAdocao, Long> {
    // você pode adicionar buscas por cidade, status etc. quando precisar
}
