package AchePetWebSite.AchePet.Repository;

import AchePetWebSite.AchePet.Model.PetPerdido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetPerdidoRepository extends JpaRepository<PetPerdido, Long> {

    List<PetPerdido> findByUsuario_CdIdUsuario(Long usuarioId);

    List<PetPerdido> findByDsStatus(String dsStatus);
}
