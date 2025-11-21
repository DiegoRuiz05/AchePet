package AchePetWebSite.AchePet.Service;

import AchePetWebSite.AchePet.Dto.PetAdocaoRequest;
import AchePetWebSite.AchePet.Dto.PetAdocaoResponse;
import AchePetWebSite.AchePet.Model.PetAdocao;
import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Repository.PetAdocaoRepository;
import AchePetWebSite.AchePet.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetAdocaoService {

    @Autowired
    private PetAdocaoRepository petAdocaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    // ============================================================
    // CADASTRAR PET ADOÇÃO
    // ============================================================
    public PetAdocaoResponse cadastrarPetAdocao(PetAdocaoRequest req, Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        PetAdocao pet = new PetAdocao();

        // Mapeamento 100% alinhado com sua entidade e DTO
        pet.setEspecie(req.getEspecie());
        pet.setNome(req.getNome());
        pet.setRaca(req.getRaca());
        pet.setPorte(req.getPorte());
        pet.setCor(req.getCor());
        pet.setIdade(req.getIdade());
        pet.setCastrado(req.getCastrado());
        pet.setVacinado(req.getVacinado());
        pet.setDisponivelEntrega(req.getDisponivelEntrega());
        pet.setDescricao(req.getDescricao());
        pet.setEstado(req.getEstado());
        pet.setCidade(req.getCidade());   // CORRIGIDO
        pet.setBairro(req.getBairro());
        pet.setTelefone(req.getTelefone());
        pet.setEmail(req.getEmail());
        pet.setDataRegistro(LocalDateTime.now());

        pet.setUsuario(usuario);

        // Upload de imagem
        if (req.getImagem() != null && !req.getImagem().isEmpty()) {
            String caminhoSalvo = salvarImagem(req.getImagem());
            pet.setCaminhoImagem(caminhoSalvo);
        }

        PetAdocao petSalvo = petAdocaoRepository.save(pet);

        return converterParaResponse(petSalvo);
    }


    // ============================================================
    // LISTAR TODOS
    // ============================================================
    public List<PetAdocaoResponse> listarTodos() {
        return petAdocaoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }


    // ============================================================
    // BUSCAR POR ID
    // ============================================================
    public PetAdocaoResponse buscarPorId(Long id) {
        PetAdocao pet = petAdocaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet não encontrado."));
        return converterParaResponse(pet);
    }


    // ============================================================
    // DELETAR
    // ============================================================
    public void deletar(Long id) {
        if (!petAdocaoRepository.existsById(id)) {
            throw new RuntimeException("Pet não encontrado para exclusão.");
        }
        petAdocaoRepository.deleteById(id);
    }


    // ============================================================
    // SALVAR IMAGEM LOCAL
    // ============================================================
    private String salvarImagem(MultipartFile imagem) {

        try {
            String pasta = "src/main/resources/static/imagens/pets/";

            File diretorio = new File(pasta);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            String nomeArquivo = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
            File destino = new File(diretorio, nomeArquivo);

            imagem.transferTo(destino);

            return "/imagens/pets/" + nomeArquivo;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage());
        }
    }



    // ============================================================
    // CONVERTER ENTIDADE → RESPONSE
    // ============================================================
    private PetAdocaoResponse converterParaResponse(PetAdocao pet) {

        PetAdocaoResponse resp = new PetAdocaoResponse();

        resp.setId(pet.getId());
        resp.setEspecie(pet.getEspecie());
        resp.setNome(pet.getNome());
        resp.setRaca(pet.getRaca());
        resp.setPorte(pet.getPorte());
        resp.setCor(pet.getCor());
        resp.setIdade(pet.getIdade());
        resp.setCastrado(pet.isCastrado());
        resp.setVacinado(pet.isVacinado());
        resp.setDisponivelEntrega(pet.isDisponivelEntrega());
        resp.setDescricao(pet.getDescricao());
        resp.setEstado(pet.getEstado());
        resp.setCidade(pet.getCidade());
        resp.setBairro(pet.getBairro());
        resp.setTelefone(pet.getTelefone());
        resp.setEmail(pet.getEmail());
        resp.setCaminhoImagem(pet.getCaminhoImagem());
        resp.setStatus(pet.getStatus());
        pet.setDataRegistro(LocalDateTime.now());

        if (pet.getDataRegistro() != null) {
            resp.setDataRegistro(
                    pet.getDataRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
        }

        return resp;
    }
}
