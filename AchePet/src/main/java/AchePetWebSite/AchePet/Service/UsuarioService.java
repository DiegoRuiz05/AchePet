package AchePetWebSite.AchePet.Service;


import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
            throw new RuntimeException("Nome de usuário já cadastrado!");
        }

        // Criptografar a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }

    public boolean autenticar(String email, String senha) {
        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(email);

        if (optUsuario.isEmpty()) {
            return false;
        }

        Usuario usuario = optUsuario.get();
        return passwordEncoder.matches(senha, usuario.getSenha());
    }
}

