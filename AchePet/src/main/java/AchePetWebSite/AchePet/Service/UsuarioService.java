package AchePetWebSite.AchePet.Service;

import AchePetWebSite.AchePet.Dto.UsuarioRequest;
import AchePetWebSite.AchePet.Dto.UsuarioResponse;
import AchePetWebSite.AchePet.Dto.UsuarioCompletoResponse;
import AchePetWebSite.AchePet.Dto.PetAdocaoResponse;

import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Model.PetAdocao;

import AchePetWebSite.AchePet.Repository.UsuarioRepository;
import AchePetWebSite.AchePet.Repository.PetAdocaoRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PetAdocaoRepository petAdocaoRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PetAdocaoRepository petAdocaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.petAdocaoRepository = petAdocaoRepository;
    }

    // ===========================================================
    // CADASTRAR
    // ===========================================================
    public UsuarioResponse cadastrarUsuario(UsuarioRequest r) {

        if (usuarioRepository.existsByNmUsuario(r.getNmUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nmUsuario já cadastrado");
        }
        if (usuarioRepository.existsByNmEmail(r.getNmEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nmEmail já cadastrado");
        }
        if (usuarioRepository.existsByCdCpf(r.getCdCpf())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cdCpf já cadastrado");
        }

        Usuario u = new Usuario();
        u.setNmNome(r.getNmNome());
        u.setNmSobrenome(r.getNmSobrenome());
        u.setNmUsuario(r.getNmUsuario());
        u.setCdCpf(r.getCdCpf());
        u.setNmEmail(r.getNmEmail());
        u.setCdTelefone(r.getCdTelefone());
        u.setDsSenha(r.getDsSenha());

        usuarioRepository.save(u);

        return new UsuarioResponse(u);
    }

    // ===========================================================
    // LOGIN
    // ===========================================================
    public UsuarioCompletoResponse login(Optional<String> nmUsuario, Optional<String> nmEmail, String senha) {

        Usuario usuario = nmUsuario
                .flatMap(usuarioRepository::findByNmUsuario)
                .or(() -> nmEmail.flatMap(usuarioRepository::findByNmEmail))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        if (!usuario.getDsSenha().equals(senha)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida");
        }

        return montarUsuarioCompleto(usuario);
    }

    // ===========================================================
    // LISTAR TODOS (COMPLETO + PETS + IMAGENS)
    // ===========================================================
    public List<UsuarioCompletoResponse> listarTodos() {

        return usuarioRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Usuario::getCdIdUsuario)) // id crescente
                .map(this::montarUsuarioCompleto)
                .collect(Collectors.toList());
    }

    // ===========================================================
    // BUSCAR POR ID (COMPLETO)
    // ===========================================================
    public UsuarioCompletoResponse buscarUsuarioComPets(Long id) {

        Usuario usuario = usuarioRepository.buscarUsuarioComPets(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        return montarUsuarioCompleto(usuario);
    }

    // ===========================================================
    // DELETAR
    // ===========================================================
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // ===========================================================
    // FUNÇÃO CENTRAL: MONTA USUÁRIO COMPLETO
    // ===========================================================
    private UsuarioCompletoResponse montarUsuarioCompleto(Usuario usuario) {

        List<PetAdocaoResponse> petsResponse = new ArrayList<>();

        for (PetAdocao p : usuario.getPets()) {

            List<String> imagens = new ArrayList<>();

            try {
                if (p.getDsCaminhoImagem() != null) {
                    imagens = mapper.readValue(
                            p.getDsCaminhoImagem(),
                            new TypeReference<List<String>>() {}
                    );
                }
            } catch (Exception ignored) {}

            petsResponse.add(new PetAdocaoResponse(p, imagens));
        }

        return new UsuarioCompletoResponse(usuario, petsResponse);
    }
}
