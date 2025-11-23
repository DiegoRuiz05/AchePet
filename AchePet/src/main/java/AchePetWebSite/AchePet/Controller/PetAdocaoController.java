package AchePetWebSite.AchePet.Controller;

import AchePetWebSite.AchePet.Dto.PetAdocaoCadastroRequest;
import AchePetWebSite.AchePet.Dto.PetAdocaoCadastroResponse;
import AchePetWebSite.AchePet.Dto.PetAdocaoImagensResponse;
import AchePetWebSite.AchePet.Dto.PetAdocaoResponse;

import AchePetWebSite.AchePet.Service.PetAdocaoService;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/adocao")
@Validated
public class PetAdocaoController {

    private final PetAdocaoService petAdocaoService;

    public PetAdocaoController(PetAdocaoService petAdocaoService) {
        this.petAdocaoService = petAdocaoService;
    }

    // ===========================================================
    // CADASTRAR PET (JSON)
    // ===========================================================
    @PostMapping(value = "/cadastrar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetAdocaoCadastroResponse> cadastrar(
            @Valid @RequestBody PetAdocaoCadastroRequest request) {

        PetAdocaoCadastroResponse resp = petAdocaoService.criarPet(request);
        return ResponseEntity.status(201).body(resp);
    }

    // ===========================================================
    // UPLOAD DE IMAGENS (MULTIPART)
    // ===========================================================
    @PostMapping(value = "/{id}/imagens", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetAdocaoImagensResponse> uploadImagens(
            @PathVariable("id") Long id,
            @RequestParam("imagens") List<MultipartFile> imagens) {

        PetAdocaoImagensResponse resp = petAdocaoService.salvarImagens(id, imagens);
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // BUSCAR PET POR ID (COM IMAGENS)
    // ===========================================================
    @GetMapping("/{id}")
    public ResponseEntity<PetAdocaoResponse> getById(@PathVariable Long id) {
        PetAdocaoResponse resp = petAdocaoService.buscarPorId(id);
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // LISTAR TODOS OS PETS
    // ===========================================================
    @GetMapping("/listar")
    public ResponseEntity<List<PetAdocaoResponse>> listarTodos() {
        List<PetAdocaoResponse> resp = petAdocaoService.listarTodos();
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // LISTAR PETS DE UM USUÁRIO
    // ===========================================================
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PetAdocaoResponse>> listarPorUsuario(
            @PathVariable Long usuarioId) {

        List<PetAdocaoResponse> resp = petAdocaoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // ATUALIZAR PET (SOMENTE DONO)
    // ===========================================================
    @PutMapping("/{id}")
    public ResponseEntity<PetAdocaoResponse> atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody PetAdocaoCadastroRequest request) {

        PetAdocaoResponse resp =
                petAdocaoService.atualizarPet(id, request, request.getCdIdUsuario());

        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // DELETAR PET (SOMENTE DONO)
    // ===========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable("id") Long id,
            @RequestParam("cdIdUsuario") Long cdIdUsuario) {

        petAdocaoService.deletarPet(id, cdIdUsuario);
        return ResponseEntity.noContent().build();
    }
}
