package AchePetWebSite.AchePet.Controller;



import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public String registrarUsuario(@RequestBody Usuario usuario) {
        usuarioService.cadastrarUsuario(usuario);
        return "Usuário cadastrado com sucesso!";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha) {
        boolean autenticado = usuarioService.autenticar(email, senha);
        return autenticado ? "Login realizado com sucesso!" : "E-mail ou senha inválidos!";
    }
}

