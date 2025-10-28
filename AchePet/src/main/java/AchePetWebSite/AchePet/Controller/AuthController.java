package AchePetWebSite.AchePet.Controller;

import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public String registrarUsuario(@RequestBody Usuario usuario) {
        usuarioService.cadastrarUsuario(usuario);
        return "Usuário cadastrado com sucesso!";
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        boolean autenticado = usuarioService.autenticar(usuario.getEmail(), usuario.getSenha());
        return autenticado ? "Login realizado com sucesso!" : "E-mail ou senha inválidos!";
    }

    // CRUD para testes
    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/usuarios/{id}")
    public Optional<Usuario> buscarUsuario(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/usuarios/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.atualizar(id, usuario);
    }

    @DeleteMapping("/usuarios/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarPorId(id);
        return "Usuário deletado com sucesso!";
    }
}
