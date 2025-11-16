package AchePetWebSite.AchePet.Controller;

import AchePetWebSite.AchePet.Model.ImagemPet;
import AchePetWebSite.AchePet.Model.PetAdocao;
import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Repository.PetAdocaoRepository;
import AchePetWebSite.AchePet.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetAdocaoController {

    @Autowired
    private PetAdocaoRepository petRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/ImagemPets/";

    @PostMapping
    public ResponseEntity<?> createPet(@RequestBody PetAdocao pet) {
        if (pet.getCdIdUsuario() == null) {
            return ResponseEntity.badRequest().body("O campo cdIdUsuario é obrigatório.");
        }

        Optional<Usuario> usuarioOpt = usuarioRepo.findById(pet.getCdIdUsuario());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado com o ID informado.");
        }

        pet.setUsuario(usuarioOpt.get());
        petRepo.save(pet);
        return ResponseEntity.ok(pet);
    }

    @PostMapping("/upload/{idPet}")
    public ResponseEntity<?> uploadPetImages(
            @PathVariable Long idPet,
            @RequestParam("imagens") List<MultipartFile> imagens) {

        try {
            Optional<PetAdocao> petOpt = petRepo.findById(idPet);
            if (petOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Pet não encontrado.");
            }

            PetAdocao pet = petOpt.get();

            int totalExistentes = pet.getImagens().size();
            if (totalExistentes + imagens.size() > 4) {
                return ResponseEntity.badRequest().body("Limite de 4 imagens por pet excedido.");
            }

            final String IMAGE_UPLOAD_DIR = System.getProperty("user.home") + "/AchePetUploads/";
            File pasta = new File(IMAGE_UPLOAD_DIR);
            if (!pasta.exists()) pasta.mkdirs();

            // Lista para retornar os links
            List<Map<String, String>> linksImagens = new java.util.ArrayList<>();

            for (MultipartFile imagem : imagens) {
                String nomeArquivo = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
                File destino = new File(IMAGE_UPLOAD_DIR + nomeArquivo);
                imagem.transferTo(destino);

                ImagemPet imgPet = new ImagemPet();
                imgPet.setNomeArquivo(nomeArquivo);
                imgPet.setUrlImagem(destino.getAbsolutePath());
                imgPet.setPetAdocao(pet);

                pet.getImagens().add(imgPet);

                // URL pública da imagem
                String urlPublica = "http://localhost:8080/imagens/" + nomeArquivo;

                linksImagens.add(
                        Map.of(
                                "nome", nomeArquivo,
                                "url", urlPublica
                        )
                );
            }

            petRepo.save(pet);

            return ResponseEntity.ok(
                    Map.of(
                            "mensagem", "Imagens salvas com sucesso!",
                            "imagens", linksImagens
                    )
            );

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar imagens: " + e.getMessage());
        }
    }


    @GetMapping("/{id}/imagens")
    public ResponseEntity<?> listarImagensDoPet(@PathVariable Long id) {
        Optional<PetAdocao> petOpt = petRepo.findById(id);
        if (petOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        PetAdocao pet = petOpt.get();

        // Retorna apenas nome e URL, codificando nomes de arquivos com espaços/acentos
        List<Map<String, String>> imagens = pet.getImagens().stream()
                .map(img -> {
                    String nomeArquivoCodificado = URLEncoder.encode(img.getNomeArquivo(), StandardCharsets.UTF_8);
                    return Map.of(
                            "idImagem", img.getId().toString(),
                            "urlImagem", "http://localhost:8080/imagens/" + nomeArquivoCodificado
                    );
                })
                .toList();

        return ResponseEntity.ok(imagens);
    }

    @DeleteMapping("/{idPet}/imagens/{idImagem}")
    public ResponseEntity<?> deletarImagem(@PathVariable Long idPet, @PathVariable Long idImagem) {
        Optional<PetAdocao> petOpt = petRepo.findById(idPet);
        if (petOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Pet não encontrado.");
        }

        PetAdocao pet = petOpt.get();

        // Busca a imagem pelo id
        Optional<ImagemPet> imgOpt = pet.getImagens().stream()
                .filter(img -> img.getId().equals(idImagem))
                .findFirst();

        if (imgOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Imagem não encontrada.");
        }

        ImagemPet imagem = imgOpt.get();

        // Remove do banco
        pet.getImagens().remove(imagem);
        petRepo.save(pet);

        // Remove arquivo físico
        final String IMAGE_UPLOAD_DIR = System.getProperty("user.home") + "/AchePetUploads/";
        File arquivo = new File(IMAGE_UPLOAD_DIR + imagem.getNomeArquivo());
        if (arquivo.exists()) {
            if (!arquivo.delete()) {
                return ResponseEntity.internalServerError().body("Erro ao deletar o arquivo da imagem.");
            }
        }

        return ResponseEntity.ok(Map.of(
                "mensagem", "Imagem deletada com sucesso!",
                "idImagem", idImagem
        ));
    }


    @GetMapping
    public ResponseEntity<?> getAllPets() {
        return ResponseEntity.ok(petRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetById(@PathVariable Long id) {
        return petRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePet(@PathVariable Long id, @RequestBody PetAdocao pet) {
        return petRepo.findById(id).map(existing -> {
            existing.setNmEspecie(pet.getNmEspecie());
            existing.setNmRaca(pet.getNmRaca());
            existing.setDsPorte(pet.getDsPorte());
            existing.setDsCor(pet.getDsCor());
            existing.setNmPet(pet.getNmPet());
            existing.setDsIdade(pet.getDsIdade());
            existing.setIcCastrado(pet.getIcCastrado());
            existing.setIcVacinado(pet.getIcVacinado());
            existing.setIcDisponivelEntrega(pet.getIcDisponivelEntrega());
            existing.setDsDescricao(pet.getDsDescricao());
            existing.setNmEstado(pet.getNmEstado());
            existing.setNmCidade(pet.getNmCidade());
            existing.setNmBairro(pet.getNmBairro());
            existing.setCdTelefone(pet.getCdTelefone());
            existing.setNmEmail(pet.getNmEmail());
            petRepo.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id) {
        if (!petRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        petRepo.deleteById(id);
        return ResponseEntity.ok("Pet deletado com sucesso!");
    }
}