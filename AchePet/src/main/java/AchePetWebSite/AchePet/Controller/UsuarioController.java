package AchePetWebSite.AchePet.Controller;

import AchePetWebSite.AchePet.Dto.UsuarioRequest;
import AchePetWebSite.AchePet.Dto.UsuarioResponse;
import AchePetWebSite.AchePet.Dto.UsuarioCompletoResponse;

import AchePetWebSite.AchePet.Service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ===========================================================
    // CADASTRAR
    // ===========================================================
    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse resp = usuarioService.cadastrarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // ===========================================================
    // LOGIN
    // ===========================================================
    @PostMapping("/logar")
    public ResponseEntity<UsuarioCompletoResponse> logar(@RequestBody Map<String, String> payload) {

        String nmUsuario = payload.get("nmUsuario");
        String nmEmail = payload.get("nmEmail");
        String dsSenha = payload.get("dsSenha");

        if ((nmUsuario == null || nmUsuario.isBlank()) &&
                (nmEmail == null || nmEmail.isBlank())) {
            return ResponseEntity.badRequest().build();
        }

        if (dsSenha == null || dsSenha.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        UsuarioCompletoResponse resp = usuarioService.login(
                Optional.ofNullable(nmUsuario),
                Optional.ofNullable(nmEmail),
                dsSenha
        );

        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // BUSCAR POR ID (RETORNA COMPLETO + PETS + IMAGENS)
    // ===========================================================
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioCompletoResponse> buscarPorId(@PathVariable Long id) {
        UsuarioCompletoResponse resp = usuarioService.buscarUsuarioComPets(id);
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // LISTAR TODOS (COM PETS E IMAGENS)
    // ===========================================================
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioCompletoResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request
    ) {
        UsuarioResponse resp = usuarioService.atualizarUsuario(id, request);
        return ResponseEntity.ok(resp);
    }

    // ===========================================================
    // DELETAR USUÁRIO
    // ===========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
