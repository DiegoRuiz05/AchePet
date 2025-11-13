package AchePetWebSite.AchePet.Repository;

import AchePetWebSite.AchePet.Model.ImagemPet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImagemPetRepository extends JpaRepository<ImagemPet, Long> {
    List<ImagemPet> findByPetAdocaoCdIdPetAdocao(Long idPet);
}
