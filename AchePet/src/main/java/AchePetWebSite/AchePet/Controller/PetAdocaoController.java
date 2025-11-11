package AchePetWebSite.AchePet.Controller;

import AchePetWebSite.AchePet.Model.PetAdocao;
import AchePetWebSite.AchePet.Model.Usuario;
import AchePetWebSite.AchePet.Repository.PetAdocaoRepository;
import AchePetWebSite.AchePet.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetAdocaoController {

    @Autowired
    private PetAdocaoRepository petRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

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



//package AchePetWebSite.AchePet.Controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import AchePetWebSite.AchePet.Model.PetAdocao;
//import AchePetWebSite.AchePet.Model.Usuario;
//import AchePetWebSite.AchePet.Repository.PetAdocaoRepository;
//import AchePetWebSite.AchePet.Repository.UsuarioRepository;
//import AchePetWebSite.AchePet.Service.AuthService;
//
//import java.net.URI;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/pets")
//public class PetAdocaoController {
//
//    private final PetAdocaoRepository petRepo;
//    private final UsuarioRepository usuarioRepo;
//    private final AuthService authService;
//
//    public PetAdocaoController(PetAdocaoRepository petRepo, UsuarioRepository usuarioRepo, AuthService authService) {
//        this.petRepo = petRepo;
//        this.usuarioRepo = usuarioRepo;
//        this.authService = authService;
//    }
//
//    // Listar todos
//    @GetMapping
//    public ResponseEntity<List<PetAdocao>> listAll() {
//        return ResponseEntity.ok(petRepo.findAll());
//    }
//
//    // Buscar por id
//    @GetMapping("/{id}")
//    public ResponseEntity<PetAdocao> getById(@PathVariable Long id) {
//        return petRepo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Criar pet (requer token do usuário criador no header "X-AUTH-TOKEN")
//    @PostMapping
//    public ResponseEntity<?> create(@RequestHeader(name = "X-AUTH-TOKEN", required = false) String token,
//                                    @RequestBody PetAdocao pet) {
//        if (token == null) {
//            return ResponseEntity.status(401).body("É necessário o header X-AUTH-TOKEN");
//        }
//        Optional<Long> userId = authService.validateToken(token);
//        if (userId.isEmpty()) {
//            return ResponseEntity.status(401).body("Token inválido");
//        }
//        Optional<Usuario> u = usuarioRepo.findById(userId.get());
//        if (u.isEmpty()) return ResponseEntity.status(401).body("Usuário não encontrado");
//        pet.setUsuario(u.get());
//        PetAdocao saved = petRepo.save(pet);
//        return ResponseEntity.created(URI.create("/api/pets/" + saved.getCdIdPetAdocao())).body(saved);
//    }
//
//    // Atualizar pet (requer token do usuário criador)
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@RequestHeader(name = "X-AUTH-TOKEN", required = false) String token,
//                                    @PathVariable Long id,
//                                    @RequestBody PetAdocao pet) {
//        if (token == null) {
//            return ResponseEntity.status(401).body("É necessário o header X-AUTH-TOKEN");
//        }
//        Optional<Long> userId = authService.validateToken(token);
//        if (userId.isEmpty()) {
//            return ResponseEntity.status(401).body("Token inválido");
//        }
//        Optional<PetAdocao> existing = petRepo.findById(id);
//        if (existing.isEmpty()) return ResponseEntity.notFound().build();
//        PetAdocao p = existing.get();
//        if (p.getUsuario() == null || !p.getUsuario().getCdIdUsuario().equals(userId.get())) {
//            return ResponseEntity.status(403).body("Só o dono pode editar o pet");
//        }
//        // atualizar campos permitidos
//        p.setNmEspecie(pet.getNmEspecie());
//        p.setNmRaca(pet.getNmRaca());
//        p.setDsPorte(pet.getDsPorte());
//        p.setDsCor(pet.getDsCor());
//        p.setNmPet(pet.getNmPet());
//        p.setDsIdade(pet.getDsIdade());
//        p.setIcCastrado(pet.getIcCastrado());
//        p.setIcVacinado(pet.getIcVacinado());
//        p.setIcDisponivelEntrega(pet.getIcDisponivelEntrega());
//        p.setDsDescricao(pet.getDsDescricao());
//        p.setNmEstado(pet.getNmEstado());
//        p.setNmCidade(pet.getNmCidade());
//        p.setNmBairro(pet.getNmBairro());
//        p.setCdTelefone(pet.getCdTelefone());
//        p.setNmEmail(pet.getNmEmail());
//        p.setDsStatus(pet.getDsStatus());
//
//        petRepo.save(p);
//        return ResponseEntity.ok(p);
//    }
//
//    // Excluir pet (só dono)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> delete(@RequestHeader(name = "X-AUTH-TOKEN", required = false) String token,
//                                    @PathVariable Long id) {
//        if (token == null) {
//            return ResponseEntity.status(401).body("É necessário o header X-AUTH-TOKEN");
//        }
//        Optional<Long> userId = authService.validateToken(token);
//        if (userId.isEmpty()) {
//            return ResponseEntity.status(401).body("Token inválido");
//        }
//        Optional<PetAdocao> existing = petRepo.findById(id);
//        if (existing.isEmpty()) return ResponseEntity.notFound().build();
//        PetAdocao p = existing.get();
//        if (p.getUsuario() == null || !p.getUsuario().getCdIdUsuario().equals(userId.get())) {
//            return ResponseEntity.status(403).body("Só o dono pode excluir o pet");
//        }
//        petRepo.delete(p);
//        return ResponseEntity.noContent().build();
//    }
//}
