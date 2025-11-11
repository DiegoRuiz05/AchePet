package AchePetWebSite.AchePet.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Repository.UsuarioRepository;
import AchePetWebSite.AchePet.Service.AuthService;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthService authService, UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }

    // registro
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuario.getNmUsuario() == null || usuario.getDsSenha() == null || usuario.getNmEmail() == null || usuario.getCdCpf() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "username, password, email e cpf são obrigatórios"));
        }
        if (usuarioRepository.existsByNmUsuario(usuario.getNmUsuario())) {
            return ResponseEntity.badRequest().body(Map.of("error", "nmUsuario já em uso"));
        }
        if (usuarioRepository.existsByNmEmail(usuario.getNmEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "email já em uso"));
        }
        if (usuarioRepository.existsByCdCpf(usuario.getCdCpf())) {
            return ResponseEntity.badRequest().body(Map.of("error", "CPF já em uso"));
        }

        Usuario saved = authService.register(usuario);
        return ResponseEntity.created(URI.create("/api/auth/user/" + saved.getCdIdUsuario())).body(saved);
    }

    // login: retorna token simples
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String nmUsuario = body.get("nmUsuario");
        String dsSenha = body.get("dsSenha");
        if (nmUsuario == null || dsSenha == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "nmUsuario e dsSenha obrigatórios"));
        }
        Optional<String> tokenOpt = authService.login(nmUsuario, dsSenha);
        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok(Map.of("token", tokenOpt.get()));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
        }
    }

    // endpoint para ver info do user por id (opcional)
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(u -> ResponseEntity.ok(u))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
