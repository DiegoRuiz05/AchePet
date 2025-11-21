package AchePetWebSite.AchePet.Controller;

import AchePetWebSite.AchePet.Dto.PetAdocaoRequest;
import AchePetWebSite.AchePet.Dto.PetAdocaoResponse;
import AchePetWebSite.AchePet.Service.PetAdocaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets/adocao")
public class PetAdocaoController {

    @Autowired
    private PetAdocaoService petAdocaoService;


    // ============================================================
    // CADASTRAR PET COM IMAGEM (MultipartFile)
    // ============================================================
    @PostMapping(
            value = "/{idUsuario}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public PetAdocaoResponse cadastrar(
            @Valid @ModelAttribute PetAdocaoRequest req,
            @PathVariable Long idUsuario
    ) {
        return petAdocaoService.cadastrarPetAdocao(req, idUsuario);
    }


    // ============================================================
    // LISTAR TODOS
    // ============================================================
    @GetMapping
    public List<PetAdocaoResponse> listarTodos() {
        return petAdocaoService.listarTodos();
    }


    // ============================================================
    // BUSCAR POR ID
    // ============================================================
    @GetMapping("/{id}")
    public PetAdocaoResponse buscarPorId(@PathVariable Long id) {
        return petAdocaoService.buscarPorId(id);
    }


    // ============================================================
    // DELETAR POR ID
    // ============================================================
    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id) {
        petAdocaoService.deletar(id);
        return "Pet excluído com sucesso.";
    }
}
