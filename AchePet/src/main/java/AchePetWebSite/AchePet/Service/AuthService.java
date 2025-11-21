package AchePetWebSite.AchePet.Service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Repository.UsuarioRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final Map<String, Long> tokenStore = new ConcurrentHashMap<>();

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario register(Usuario usuario) {
        // hash da senha antes de salvar
        String hashed = BCrypt.hashpw(usuario.getDsSenha(), BCrypt.gensalt());
        usuario.setDsSenha(hashed);
        return usuarioRepository.save(usuario);
    }

    public Optional<String> login(String nmUsuario, String dsSenha) {
        Optional<Usuario> userOpt = usuarioRepository.findByNmUsuario(nmUsuario);
        if (userOpt.isPresent()) {
            Usuario u = userOpt.get();
            if (BCrypt.checkpw(dsSenha, u.getDsSenha())) {
                String token = UUID.randomUUID().toString();
                tokenStore.put(token, u.getCdIdUsuario());
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    public Optional<Long> validateToken(String token) {
        return Optional.ofNullable(tokenStore.get(token));
    }

    public void logout(String token) {
        tokenStore.remove(token);
    }
}
