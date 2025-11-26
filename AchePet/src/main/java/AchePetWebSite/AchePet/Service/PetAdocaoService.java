package AchePetWebSite.AchePet.Service;

import AchePetWebSite.AchePet.Dto.PetAdocaoCadastroRequest;
import AchePetWebSite.AchePet.Dto.PetAdocaoCadastroResponse;
import AchePetWebSite.AchePet.Dto.PetAdocaoImagensResponse;
import AchePetWebSite.AchePet.Dto.PetAdocaoResponse;
import AchePetWebSite.AchePet.Model.PetAdocao;
import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Repository.PetAdocaoRepository;
import AchePetWebSite.AchePet.Repository.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PetAdocaoService {

    private final PetAdocaoRepository petAdocaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper mapper;

    @Value("${achepet.upload.dir:uploads/pets/}")
    private String uploadDir;

    public PetAdocaoService(
            PetAdocaoRepository petAdocaoRepository,
            UsuarioRepository usuarioRepository,
            ObjectMapper mapper
    ) {
        this.petAdocaoRepository = petAdocaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    // ============================================================
    // 1) Criar PET (somente JSON)
    // ============================================================
    public PetAdocaoCadastroResponse criarPet(PetAdocaoCadastroRequest req) {

        Usuario usuario = usuarioRepository.findById(req.getCdIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        PetAdocao pet = new PetAdocao();

        pet.setNmEspecie(req.getNmEspecie());
        pet.setNmRaca(req.getNmRaca());
        pet.setDsPorte(req.getDsPorte());
        pet.setDsCor(req.getDsCor());
        pet.setNmPet(req.getNmPet());
        pet.setDsIdade(req.getDsIdade());
        pet.setIcCastrado(req.getIcCastrado());
        pet.setIcVacinado(req.getIcVacinado());
        pet.setIcDisponivelEntrega(req.getIcDisponivelEntrega());
        pet.setDsDescricao(req.getDsDescricao());
        pet.setNmEstado(req.getNmEstado());
        pet.setNmCidade(req.getNmCidade());
        pet.setNmBairro(req.getNmBairro());
        pet.setCdTelefone(req.getCdTelefone());
        pet.setNmEmail(req.getNmEmail());
        pet.setDsStatus(req.getDsStatus() != null ? req.getDsStatus() : "ATIVO");
        pet.setDtRegistro(LocalDateTime.now());
        pet.setUsuario(usuario);

        PetAdocao salvo = petAdocaoRepository.save(pet);

        return new PetAdocaoCadastroResponse(salvo.getCdIdPetAdocao(), "Pet cadastrado com sucesso");
    }

    // ============================================================
    // 2) Upload de IMAGENS (multipart) — salva JSON limpo (array)
    // ============================================================
    public PetAdocaoImagensResponse salvarImagens(Long petId, List<MultipartFile> imagens) {

        if (imagens == null || imagens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pelo menos uma imagem é obrigatória");
        }

        if (imagens.size() > 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Máximo de 4 imagens permitidas");
        }

        PetAdocao pet = petAdocaoRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));

        ensureUploadDirExists();

        List<String> novosCaminhos = new ArrayList<>();
        int index = 1;

        for (MultipartFile file : imagens) {

            if (file == null || file.isEmpty()) continue;

            String original = StringUtils.cleanPath(file.getOriginalFilename());
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot);

            String filename = petId + "_" + index++ + ext;

            Path destino = Paths.get(uploadDir).resolve(filename).normalize();

            try {
                Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao salvar a imagem");
            }

            // --------------------------------------------------------
            // AJUSTE AQUI — SALVAR SOMENTE URL PÚBLICA:
            // /uploads/pets/1_1.jpg
            // --------------------------------------------------------
            String caminhoPublico = "/uploads/pets/" + filename;

            novosCaminhos.add(caminhoPublico);
        }

        // Manter imagens antigas (já em formato público ou não)
        List<String> imagensExistentes = new ArrayList<>();
        if (pet.getDsCaminhoImagem() != null && !pet.getDsCaminhoImagem().isBlank()) {
            imagensExistentes = safeParseImageJson(pet.getDsCaminhoImagem());
        }

        imagensExistentes.addAll(novosCaminhos);

        try {
            String jsonFinal = mapper.writeValueAsString(imagensExistentes);
            pet.setDsCaminhoImagem(jsonFinal);
            petAdocaoRepository.save(pet);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar lista de imagens");
        }

        return new PetAdocaoImagensResponse(petId, "Imagens enviadas com sucesso", imagensExistentes);
    }



    //Deletar imagem
    public PetAdocaoImagensResponse deletarImagem(Long petId, Long cdIdUsuario, String caminhoImagem) {

        PetAdocao pet = petAdocaoRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));

        // Verifica se o pet pertence ao usuário
        if (pet.getUsuario() == null || !pet.getUsuario().getCdIdUsuario().equals(cdIdUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar este pet");
        }

        // Carregar imagens atuais
        List<String> imagensAtuais = safeParseImageJson(pet.getDsCaminhoImagem());

        if (!imagensAtuais.contains(caminhoImagem)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagem não encontrada");
        }

        // Remove imagem da lista
        imagensAtuais.remove(caminhoImagem);

        try {
            // Salvar lista atualizada
            String json = mapper.writeValueAsString(imagensAtuais);
            pet.setDsCaminhoImagem(json);
            petAdocaoRepository.save(pet);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar imagens");
        }

        // Excluir arquivo físico (opcional, mas recomendado)
        try {
            Path filePath = Paths.get(caminhoImagem).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {
        }

        return new PetAdocaoImagensResponse(
                petId,
                "Imagem removida com sucesso",
                imagensAtuais
        );
    }


    // ============================================================
    // 3) Buscar por ID (com imagens)
    // ============================================================
    public PetAdocaoResponse buscarPorId(Long id) {

        PetAdocao pet = petAdocaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));

        List<String> imagens = new ArrayList<>();
        if (pet.getDsCaminhoImagem() != null && !pet.getDsCaminhoImagem().isBlank()) {
            imagens = safeParseImageJson(pet.getDsCaminhoImagem());
        }

        return new PetAdocaoResponse(pet, imagens);
    }

    // ============================================================
    // 4) Listar todos
    // ============================================================
    public List<PetAdocaoResponse> listarTodos() {
        List<PetAdocao> lista = petAdocaoRepository.findAll();
        List<PetAdocaoResponse> resposta = new ArrayList<>();

        for (PetAdocao p : lista) {
            List<String> imagens = new ArrayList<>();
            if (p.getDsCaminhoImagem() != null && !p.getDsCaminhoImagem().isBlank()) {
                imagens = safeParseImageJson(p.getDsCaminhoImagem());
            }
            resposta.add(new PetAdocaoResponse(p, imagens));
        }

        return resposta;
    }

    // ============================================================
    // 5) Listar por usuário
    // ============================================================
    public List<PetAdocaoResponse> listarPorUsuario(Long usuarioId) {

        List<PetAdocao> lista = petAdocaoRepository.findByUsuario_CdIdUsuario(usuarioId);

        List<PetAdocaoResponse> resposta = new ArrayList<>();

        for (PetAdocao p : lista) {
            List<String> imagens = new ArrayList<>();
            if (p.getDsCaminhoImagem() != null && !p.getDsCaminhoImagem().isBlank()) {
                imagens = safeParseImageJson(p.getDsCaminhoImagem());
            }
            resposta.add(new PetAdocaoResponse(p, imagens));
        }

        return resposta;
    }

    // ============================================================
    // 6) Atualizar PET (somente dono)
    // ============================================================
    public PetAdocaoResponse atualizarPet(Long id, PetAdocaoCadastroRequest req, Long cdIdUsuario) {

        PetAdocao pet = petAdocaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));

        if (pet.getUsuario() == null || !pet.getUsuario().getCdIdUsuario().equals(cdIdUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar este pet");
        }

        // ============================================================
        // PRESERVAR IMAGENS EXISTENTES (ANTES DE ALTERAR O PET)
        // ============================================================
        List<String> imagensAtuais = new ArrayList<>();
        if (pet.getDsCaminhoImagem() != null && !pet.getDsCaminhoImagem().isBlank()) {
            imagensAtuais = safeParseImageJson(pet.getDsCaminhoImagem());
        }

        // ============================================================
        // ATUALIZAR CAMPOS DO PET
        // ============================================================
        pet.setNmEspecie(req.getNmEspecie());
        pet.setNmRaca(req.getNmRaca());
        pet.setDsPorte(req.getDsPorte());
        pet.setDsCor(req.getDsCor());
        pet.setNmPet(req.getNmPet());
        pet.setDsIdade(req.getDsIdade());
        pet.setIcCastrado(req.getIcCastrado());
        pet.setIcVacinado(req.getIcVacinado());
        pet.setIcDisponivelEntrega(req.getIcDisponivelEntrega());
        pet.setDsDescricao(req.getDsDescricao());
        pet.setNmEstado(req.getNmEstado());
        pet.setNmCidade(req.getNmCidade());
        pet.setNmBairro(req.getNmBairro());
        pet.setCdTelefone(req.getCdTelefone());
        pet.setNmEmail(req.getNmEmail());
        pet.setDsStatus(req.getDsStatus());

        // ============================================================
        // RESTAURAR A LISTA DE IMAGENS APÓS A ATUALIZAÇÃO
        // ============================================================
        try {
            String jsonImagens = mapper.writeValueAsString(imagensAtuais);
            pet.setDsCaminhoImagem(jsonImagens);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao restaurar imagens do pet");
        }

        // ============================================================
        // SALVAR O PET ATUALIZADO
        // ============================================================
        PetAdocao atualizado = petAdocaoRepository.save(pet);

        // Preparar lista final de imagens para resposta
        List<String> imagens = new ArrayList<>();
        if (atualizado.getDsCaminhoImagem() != null && !atualizado.getDsCaminhoImagem().isBlank()) {
            imagens = safeParseImageJson(atualizado.getDsCaminhoImagem());
        }

        return new PetAdocaoResponse(atualizado, imagens);
    }


    // ============================================================
    // 7) Deletar PET (somente dono)
    // ============================================================
    public void deletarPet(Long id, Long cdIdUsuario) {

        PetAdocao pet = petAdocaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));

        if (pet.getUsuario() == null || !pet.getUsuario().getCdIdUsuario().equals(cdIdUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir este pet");
        }

        petAdocaoRepository.deleteById(id);
    }

    // ============================================================
    // Utilitário: Criar diretório caso não exista
    // ============================================================
    private void ensureUploadDirExists() {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao criar pasta de upload");
            }
        }
    }

    // ============================================================
    // Utilitário: desserializar dsCaminhoImagem com tolerância
    // - aceita JSON puro (["a","b"])
    // - aceita string duplamente serializada ("[\"a\",\"b\"]")
    // Retorna lista vazia em caso de falha
    // ============================================================
    private List<String> safeParseImageJson(String raw) {
        if (raw == null || raw.isBlank()) return new ArrayList<>();

        // 1) Se já for um array JSON (começa com '['), parse direto
        String trimmed = raw.trim();
        try {
            if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                return mapper.readValue(trimmed, new TypeReference<List<String>>() {});
            }

            // 2) Caso esteja duplamente serializado: exemplo -> "\"[\\\"uploads/...\\\"]\""
            // mapper.readValue(raw, String.class) remove as aspas externas/escapes
            if ((trimmed.startsWith("\"[") && trimmed.endsWith("]\"")) || trimmed.startsWith("\"")) {
                try {
                    String unescaped = mapper.readValue(raw, String.class);
                    if (unescaped != null) {
                        String uTrim = unescaped.trim();
                        if (uTrim.startsWith("[") && uTrim.endsWith("]")) {
                            return mapper.readValue(uTrim, new TypeReference<List<String>>() {});
                        }
                    }
                } catch (Exception inner) {
                    // fallback to attempt to strip surrounding quotes manually
                    String manual = trimmed;
                    if (manual.startsWith("\"") && manual.endsWith("\"")) {
                        manual = manual.substring(1, manual.length() - 1);
                        // replace escaped quotes
                        manual = manual.replace("\\\"", "\"");
                        manual = manual.replace("\\\\", "\\");
                    }
                    if (manual.startsWith("[") && manual.endsWith("]")) {
                        try {
                            return mapper.readValue(manual, new TypeReference<List<String>>() {});
                        } catch (Exception ex) {
                            // fallthrough to return empty list
                        }
                    }
                }
            }

        } catch (Exception e) {
            // se qualquer erro, não repassa exceção, apenas retorna lista vazia
        }

        return new ArrayList<>();
    }
}
