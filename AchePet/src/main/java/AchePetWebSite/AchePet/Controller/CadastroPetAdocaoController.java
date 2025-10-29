package AchePetWebSite.AchePet.Controller;

import AchePetWebSite.AchePet.Model.CadastroPetAdocao;
import AchePetWebSite.AchePet.Service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets/adocao")
@CrossOrigin(origins = "*")
public class CadastroPetAdocaoController {

    @Autowired
    private PetService petService;

    @PostMapping
    public CadastroPetAdocao criarPetAdocao(@RequestBody CadastroPetAdocao cadastroPetAdocao) {
        return petService.salvar(cadastroPetAdocao);
    }

    @GetMapping
    public List<CadastroPetAdocao> listarPetsAdocao() {
        return petService.listarTodos();
    }

    @GetMapping("/{id}")
    public CadastroPetAdocao buscarPorId(@PathVariable Long id) {
        return petService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public CadastroPetAdocao atualizarPetsAdocao(@PathVariable Long id, @RequestBody CadastroPetAdocao cadastroPetAdocao) {
        return petService.atualizar(id, cadastroPetAdocao);
    }

    @DeleteMapping("/{id}")
    public void deletarPetsAdocao(@PathVariable Long id) {
        petService.deletarPorId(id);
    }
}//classe de cadastro do pet de adocao
