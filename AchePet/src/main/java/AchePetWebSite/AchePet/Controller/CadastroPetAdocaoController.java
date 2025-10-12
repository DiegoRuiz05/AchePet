package AchePetWebSite.AchePet.Controller;


import AchePetWebSite.AchePet.Model.CadastroPetAdocao;

import AchePetWebSite.AchePet.Service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/petsParaAdocao")

public class CadastroPetAdocaoController {

    @Autowired
    private PetService petService;

    @PostMapping
    public CadastroPetAdocao criarPetAdoção(@RequestBody CadastroPetAdocao cadastroPetAdoção) {
        return petService.salvar(cadastroPetAdoção);
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
    public CadastroPetAdocao atualizarPetsAdocao(@PathVariable Long id, @RequestBody CadastroPetAdocao cadastroPetAdoção) {
        return petService.atualizar(id, cadastroPetAdoção);
    }


    @DeleteMapping("/{id}")
    public void deletarPetsAdocao(@PathVariable Long id) {
        petService.deletarPorId(id);
    }
}

