package AchePetWebSite.AchePet.Service;

import AchePetWebSite.AchePet.Dto.PetPerdidoCadastroRequest;
import AchePetWebSite.AchePet.Dto.PetPerdidoCadastroResponse;
import AchePetWebSite.AchePet.Dto.PetPerdidoImagensResponse;
import AchePetWebSite.AchePet.Dto.PetPerdidoResponse;
import AchePetWebSite.AchePet.Model.PetPerdido;
import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Repository.PetPerdidoRepository;
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
public class PetPerdidoService {

    private final PetPerdidoRepository petPerdidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper mapper;

    // PASTA CORRETA PARA PETS PERDIDOS
    @Value("${achepet.upload.dir.perdido:uploads/pets-perdidos/}")
    private String uploadDir;

    public PetPerdidoService(
            PetPerdidoRepository petPerdidoRepository,
            UsuarioRepository usuarioRepository,
            ObjectMapper mapper
    ) {
        this.petPerdidoRepository = petPerdidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    // ============================================================
    // 1) Criar Pet Perdido
    // ============================================================
    public PetPerdidoCadastroResponse criarPet(PetPerdidoCadastroRequest req) {

        Usuario usuario = usuarioRepository.findById(req.getCdIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        PetPerdido pet = new PetPerdido();

        pet.setNmEspecie(req.getNmEspecie());
        pet.setNmRaca(req.getNmRaca());
        pet.setDsPorte(req.getDsPorte());
        pet.setDsCor(req.getDsCor());
        pet.setNmPet(req.getNmPet());
        pet.setDsIdade(req.getDsIdade());
        pet.setDsDescricao(req.getDsDescricao());
        pet.setDtPerdido(req.getDtPerdido());
        pet.setHrPerdido(req.getHrPerdido());
        pet.setNmEstado(req.getNmEstado());
        pet.setNmCidade(req.getNmCidade());
        pet.setNmBairro(req.getNmBairro());
        pet.setCdTelefone(req.getCdTelefone());
        pet.setNmEmail(req.getNmEmail());
        pet.setDsStatus(req.getDsStatus() != null ? req.getDsStatus() : "ATIVO");
        pet.setDtRegistro(LocalDateTime.now());
        pet.setUsuario(usuario);

        PetPerdido salvo = petPerdidoRepository.save(pet);

        return new PetPerdidoCadastroResponse(salvo.getCdIdPetPerdido(), "Pet perdido cadastrado com sucesso");
    }

    // ============================================================
    // 2) Upload de imagens
    // ============================================================
    public PetPerdidoImagensResponse salvarImagens(Long petId, List<MultipartFile> imagens) {

        if (imagens == null || imagens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pelo menos uma imagem é obrigatória");
        }

        if (imagens.size() > 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Máximo de 4 imagens permitidas");
        }

        PetPerdido pet = petPerdidoRepository.findById(petId)
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

            // Salvar como URL pública: /uploads/pets-perdidos/2_1.jpg
            String caminhoPublico = "/uploads/pets-perdidos/" + filename;

            novosCaminhos.add(caminhoPublico);
        }

        List<String> imagensExistentes = new ArrayList<>();
        if (pet.getDsCaminhoImagem() != null && !pet.getDsCaminhoImagem().isBlank()) {
            imagensExistentes = safeParseImageJson(pet.getDsCaminhoImagem());
        }

        imagensExistentes.addAll(novosCaminhos);

        try {
            pet.setDsCaminhoImagem(mapper.writeValueAsString(imagensExistentes));
            petPerdidoRepository.save(pet);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar lista de imagens");
        }

        return new PetPerdidoImagensResponse(petId, "Imagens enviadas com sucesso", imagensExistentes);
    }

    // ============================================================
    // 3) Buscar por ID
    // ============================================================
    public PetPerdidoResponse buscarPorId(Long id) {

        PetPerdido pet = petPerdidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));

        List<String> imagens = new ArrayList<>();
        if (pet.getDsCaminhoImagem() != null && !pet.getDsCaminhoImagem().isBlank()) {
            imagens = safeParseImageJson(pet.getDsCaminhoImagem());
        }

        return new PetPerdidoResponse(pet, imagens);
    }

    // ============================================================
    // 4) Listar todos
    // ============================================================
    public List<PetPerdidoResponse> listarTodos() {
        List<PetPerdido> lista = petPerdidoRepository.findAll();
        List<PetPerdidoResponse> resposta = new ArrayList<>();

        for (PetPerdido p : lista) {
            List<String> imagens = new ArrayList<>();
            if (p.getDsCaminhoImagem() != null && !p.getDsCaminhoImagem().isBlank()) {
                imagens = safeParseImageJson(p.getDsCaminhoImagem());
            }
            resposta.add(new PetPerdidoResponse(p, imagens));
        }

        return resposta;
    }

    // ============================================================
    // 5) Listar por usuário
    // ============================================================
    public List<PetPerdidoResponse> listarPorUsuario(Long usuarioId) {

        List<PetPerdido> lista = petPerdidoRepository.findByUsuario_CdIdUsuario(usuarioId);

        List<PetPerdidoResponse> resposta = new ArrayList<>();

        for (PetPerdido p : lista) {
            List<String> imagens = new ArrayList<>();
            if (p.getDsCaminhoImagem() != null && !p.getDsCaminhoImagem().isBlank()) {
                imagens = safeParseImageJson(p.getDsCaminhoImagem());
            }
            resposta.add(new PetPerdidoResponse(p, imagens));
        }

        return resposta;
    }

    // ============================================================
    // 6) Atualizar PET (somente texto)
    // ============================================================
    public PetPerdidoResponse atualizarPet(Long id, PetPerdidoCadastroRequest req, Long cdIdUsuario) {

        PetPerdido pet = petPerdidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));

        if (pet.getUsuario() == null || !pet.getUsuario().getCdIdUsuario().equals(cdIdUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar este pet");
        }

        String imagensAtuais = pet.getDsCaminhoImagem();

        pet.setNmEspecie(req.getNmEspecie());
        pet.setNmRaca(req.getNmRaca());
        pet.setDsPorte(req.getDsPorte());
        pet.setDsCor(req.getDsCor());
        pet.setNmPet(req.getNmPet());
        pet.setDsIdade(req.getDsIdade());
        pet.setDsDescricao(req.getDsDescricao());
        pet.setDtPerdido(req.getDtPerdido());
        pet.setHrPerdido(req.getHrPerdido());
        pet.setNmEstado(req.getNmEstado());
        pet.setNmCidade(req.getNmCidade());
        pet.setNmBairro(req.getNmBairro());
        pet.setCdTelefone(req.getCdTelefone());
        pet.setNmEmail(req.getNmEmail());
        pet.setDsStatus(req.getDsStatus());

        pet.setDsCaminhoImagem(imagensAtuais);

        PetPerdido atualizado = petPerdidoRepository.save(pet);

        List<String> imagens = new ArrayList<>();
        if (atualizado.getDsCaminhoImagem() != null && !atualizado.getDsCaminhoImagem().isBlank()) {
            imagens = safeParseImageJson(atualizado.getDsCaminhoImagem());
        }

        return new PetPerdidoResponse(atualizado, imagens);
    }

    // ============================================================
    // 7) Deletar PET
    // ============================================================
    public void deletarPet(Long id, Long cdIdUsuario) {

        PetPerdido pet = petPerdidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));

        if (pet.getUsuario() == null || !pet.getUsuario().getCdIdUsuario().equals(cdIdUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir este pet");
        }

        try {
            List<String> imagens = safeParseImageJson(pet.getDsCaminhoImagem());
            for (String caminho : imagens) {
                try {
                    Path p = Paths.get(caminho);
                    Files.deleteIfExists(p);
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}

        petPerdidoRepository.deleteById(id);
    }

    // ============================================================
    // 8) DELETE de imagem específica (VERSÃO CORRIGIDA)
    // ============================================================
    public PetPerdidoImagensResponse deletarImagem(Long petId, String nomeArquivo, Long cdIdUsuario) {

        PetPerdido pet = petPerdidoRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));

        if (pet.getUsuario() == null || !pet.getUsuario().getCdIdUsuario().equals(cdIdUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar este pet");
        }

        List<String> imagens = safeParseImageJson(pet.getDsCaminhoImagem());

        // NORMALIZA O NOME DO ARQUIVO SOLICITADO
        String solicitado = nomeArquivo.replaceFirst("^/+", "");

        // LOCALIZA A IMAGEM EXATA NO JSON
        String alvo = imagens.stream()
                .filter(img -> img.replaceFirst("^/+", "").endsWith(solicitado))
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagem não encontrada"));

        // Remove do JSON
        imagens.remove(alvo);

        // Atualiza no banco
        try {
            pet.setDsCaminhoImagem(mapper.writeValueAsString(imagens));
            petPerdidoRepository.save(pet);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar lista de imagens");
        }

        // Apaga do disco
        try {
            String nomeFisico = Paths.get(alvo).getFileName().toString();
            Path path = Paths.get(uploadDir).resolve(nomeFisico).normalize();
            Files.deleteIfExists(path);
        } catch (Exception ignored) {}

        return new PetPerdidoImagensResponse(petId, "Imagem removida com sucesso", imagens);
    }

    // ============================================================
    // Utils
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

    private List<String> safeParseImageJson(String raw) {
        if (raw == null || raw.isBlank()) return new ArrayList<>();

        String trimmed = raw.trim();
        try {
            if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                return mapper.readValue(trimmed, new TypeReference<List<String>>() {});
            }

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
                    String manual = trimmed;
                    if (manual.startsWith("\"") && manual.endsWith("\"")) {
                        manual = manual.substring(1, manual.length() - 1);
                        manual = manual.replace("\\\"", "\"");
                        manual = manual.replace("\\\\", "\\");
                    }
                    if (manual.startsWith("[") && manual.endsWith("]")) {
                        try {
                            return mapper.readValue(manual, new TypeReference<List<String>>() {});
                        } catch (Exception ex) {}
                    }
                }
            }
        } catch (Exception e) {}

        return new ArrayList<>();
    }
}
