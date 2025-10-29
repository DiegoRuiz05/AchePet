package AchePetWebSite.AchePet.Service;

import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
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
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public boolean autenticar(String email, String senha) {
        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(email);
        return optUsuario.isPresent() && passwordEncoder.matches(senha, optUsuario.get().getSenha());
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void deletarPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario atualizar(Long id, Usuario atualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNome(atualizado.getNome());
            usuario.setSobrenome(atualizado.getSobrenome());
            usuario.setUsuario(atualizado.getUsuario());
            usuario.setCpf(atualizado.getCpf());
            usuario.setEmail(atualizado.getEmail());
            usuario.setTelefone(atualizado.getTelefone());
            usuario.setCep(atualizado.getCep());
            usuario.setEstado(atualizado.getEstado());
            usuario.setCidade(atualizado.getCidade());
            usuario.setBairro(atualizado.getBairro());
            if (atualizado.getSenha() != null && !atualizado.getSenha().isEmpty()) {
                usuario.setSenha(passwordEncoder.encode(atualizado.getSenha()));
            }
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }
}
