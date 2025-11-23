package AchePetWebSite.AchePet.Service;

import AchePetWebSite.AchePet.Dto.UsuarioRequest;
import AchePetWebSite.AchePet.Dto.UsuarioResponse;
import AchePetWebSite.AchePet.Dto.UsuarioCompletoResponse;
import AchePetWebSite.AchePet.Dto.PetAdocaoResponse;

import AchePetWebSite.AchePet.Repository.PetPerdidoRepository;
import AchePetWebSite.AchePet.Dto.PetPerdidoResponse;

import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Model.PetAdocao;

import AchePetWebSite.AchePet.Repository.UsuarioRepository;
import AchePetWebSite.AchePet.Repository.PetAdocaoRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PetAdocaoRepository petAdocaoRepository;
    private final PetPerdidoRepository petPerdidoRepository;


    private final ObjectMapper mapper = new ObjectMapper();

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PetAdocaoRepository petAdocaoRepository,
            PetPerdidoRepository petPerdidoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.petAdocaoRepository = petAdocaoRepository;
        this.petPerdidoRepository = petPerdidoRepository;
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
    @Transactional
    public UsuarioCompletoResponse login(Optional<String> nmUsuario, Optional<String> nmEmail, String senha) {

        Usuario usuario = nmUsuario.isPresent()
                ? usuarioRepository.findByNmUsuario(nmUsuario.get())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"))
                : usuarioRepository.findByNmEmail(nmEmail.get())
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

        return usuarioRepository.buscarTodosComPets()
                .stream()
                .sorted(Comparator.comparingLong(Usuario::getCdIdUsuario))
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

        // -----------------------------------------------------------
        // 1) Pets para Adoção
        // -----------------------------------------------------------
        List<PetAdocaoResponse> petsAdocaoResponse = new ArrayList<>();

        for (PetAdocao p : usuario.getPets()) {

            List<String> imagens = new ArrayList<>();

            try {
                String raw = p.getDsCaminhoImagem();

                if (raw != null && !raw.isBlank()) {

                    if (raw.startsWith("\"") && raw.endsWith("\"")) {
                        raw = raw.substring(1, raw.length() - 1);
                        raw = raw.replace("\\\"", "\"");
                    }

                    imagens = mapper.readValue(raw, new TypeReference<List<String>>() {});
                }

            } catch (Exception e) {
                System.out.println("Erro ao desserializar imagens PetAdocao: " + e.getMessage());
            }

            petsAdocaoResponse.add(new PetAdocaoResponse(p, imagens));
        }


        // -----------------------------------------------------------
        // 2) Pets Perdidos (NOVO)
        // -----------------------------------------------------------
        List<PetPerdidoResponse> petsPerdidosResponse = new ArrayList<>();

        var petsPerdidos = petPerdidoRepository.findByUsuario_CdIdUsuario(usuario.getCdIdUsuario());

        for (var p : petsPerdidos) {

            List<String> imagens = new ArrayList<>();

            try {
                String raw = p.getDsCaminhoImagem();

                if (raw != null && !raw.isBlank()) {

                    if (raw.startsWith("\"") && raw.endsWith("\"")) {
                        raw = raw.substring(1, raw.length() - 1);
                        raw = raw.replace("\\\"", "\"");
                    }

                    imagens = mapper.readValue(raw, new TypeReference<List<String>>() {});
                }

            } catch (Exception e) {
                System.out.println("Erro ao desserializar imagens PetPerdido: " + e.getMessage());
            }

            petsPerdidosResponse.add(new PetPerdidoResponse(p, imagens));
        }


        // -----------------------------------------------------------
        // 3) Retorno COMPLETO (adoção + perdido)
        // -----------------------------------------------------------
        return new UsuarioCompletoResponse(
                usuario,
                petsAdocaoResponse,
                petsPerdidosResponse
        );
    }


    public UsuarioResponse atualizarUsuario(Long id, UsuarioRequest r) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        // Somente estes 3 campos podem ser atualizados
        u.setNmNome(r.getNmNome());
        u.setNmSobrenome(r.getNmSobrenome());
        u.setCdTelefone(r.getCdTelefone());

        usuarioRepository.save(u);

        return new UsuarioResponse(u);
    }
}
