package AchePetWebSite.AchePet.Controller;

import AchePetWebSite.AchePet.Dto.PetPerdidoCadastroRequest;
import AchePetWebSite.AchePet.Dto.PetPerdidoCadastroResponse;
import AchePetWebSite.AchePet.Dto.PetPerdidoImagensResponse;
import AchePetWebSite.AchePet.Dto.PetPerdidoResponse;
import AchePetWebSite.AchePet.Service.PetPerdidoService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller para o módulo Pet Perdido.
 *
 * Rotas espelham o padrão do módulo PetAdocao, mudando apenas o prefixo para "pet-perdido".
 *
 * - POST   /pet-perdido/cadastrar                 -> criar (JSON)
 * - POST   /pet-perdido/{id}/imagem/adicionar     -> adicionar imagens (multipart)
 * - DELETE /pet-perdido/{id}/imagem/deletar       -> deletar imagem específica
 * - GET    /pet-perdido/{id}                      -> buscar por id (com imagens)
 * - GET    /pet-perdido/listar                    -> listar todos
 * - GET    /pet-perdido/usuario/{usuarioId}       -> listar por usuário
 * - PUT    /pet-perdido/{id}                      -> atualizar (dados textuais, não imagens)
 * - DELETE /pet-perdido/{id}?cdIdUsuario={id}     -> deletar (somente dono)
 *
 * Observações:
 * - As assinaturas dos métodos e nomes de DTOs seguem o padrão solicitado.
 * - A adição/exclusão de imagens não sobrescreve o JSON existente: é gerenciado no Service.
 */
@RestController
@RequestMapping("/pet-perdido")
@Validated
public class PetPerdidoController {

    private final PetPerdidoService petPerdidoService;

    public PetPerdidoController(PetPerdidoService petPerdidoService) {
        this.petPerdidoService = petPerdidoService;
    }

    // ===========================================================
    // CADASTRAR PET PERDIDO (JSON)
    // ===========================================================
    @PostMapping(value = "/cadastrar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetPerdidoCadastroResponse> cadastrar(
            @Valid @RequestBody PetPerdidoCadastroRequest request) {

        PetPerdidoCadastroResponse resp = petPerdidoService.criarPet(request);
        return ResponseEntity.status(201).body(resp);
    }

    // ===========================================================
    // ADICIONAR IMAGENS (MULTIPART) — anexa sem sobrescrever
    // POST /pet-perdido/{id}/imagem/adicionar
    // form-data: key "imagens" -> 1..4 files
    // ===========================================================
    @PostMapping(value = "/{id}/imagem/adicionar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetPerdidoImagensResponse> adicionarImagens(
            @PathVariable("id") Long id,
            @RequestParam("imagens") List<MultipartFile> imagens) {

        PetPerdidoImagensResponse resp = petPerdidoService.salvarImagens(id, imagens);
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // REMOVER IMAGEM ESPECÍFICA
    // DELETE /pet-perdido/{id}/imagem/deletar?caminho={path}&cdIdUsuario={userId}
    // ===========================================================
    @DeleteMapping("/{id}/imagem/deletar")
    public ResponseEntity<PetPerdidoImagensResponse> deletarImagem(
            @PathVariable("id") Long id,
            @RequestParam("caminho") String caminhoImagem,
            @RequestParam("cdIdUsuario") Long cdIdUsuario) {

        PetPerdidoImagensResponse resp = petPerdidoService.deletarImagem(id, caminhoImagem, cdIdUsuario);
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // BUSCAR POR ID (COM IMAGENS)
    // GET /pet-perdido/{id}
    // ===========================================================
    @GetMapping("/{id}")
    public ResponseEntity<PetPerdidoResponse> getById(@PathVariable Long id) {
        PetPerdidoResponse resp = petPerdidoService.buscarPorId(id);
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // LISTAR TODOS
    // GET /pet-perdido/listar
    // ===========================================================
    @GetMapping("/listar")
    public ResponseEntity<List<PetPerdidoResponse>> listarTodos() {
        List<PetPerdidoResponse> resp = petPerdidoService.listarTodos();
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // LISTAR POR USUÁRIO
    // GET /pet-perdido/usuario/{usuarioId}
    // ===========================================================
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PetPerdidoResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<PetPerdidoResponse> resp = petPerdidoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // ATUALIZAR PET PERDIDO (SOMENTE DADOS TEXTUAIS — NÃO ALTERA IMAGENS)
    // PUT /pet-perdido/{id}
    // Requer que o JSON contenha cdIdUsuario para validação de proprietário
    // ===========================================================
    @PutMapping("/{id}")
    public ResponseEntity<PetPerdidoResponse> atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody PetPerdidoCadastroRequest request) {

        PetPerdidoResponse resp = petPerdidoService.atualizarPet(id, request, request.getCdIdUsuario());
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // DELETAR PET PERDIDO (SOMENTE DONO)
    // DELETE /pet-perdido/{id}?cdIdUsuario={id}
    // ===========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable("id") Long id,
            @RequestParam("cdIdUsuario") Long cdIdUsuario) {

        petPerdidoService.deletarPet(id, cdIdUsuario);
        return ResponseEntity.noContent().build();
    }
}
