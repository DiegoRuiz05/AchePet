package AchePetWebSite.AchePet.Service;




import AchePetWebSite.AchePet.Model.CadastroPetAdocao;
import AchePetWebSite.AchePet.Repository.CadastroPetAdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PetService {

    @Autowired
    private CadastroPetAdocaoRepository petAdoçãoRepository;

    public CadastroPetAdocao salvar (CadastroPetAdocao cadastroPetAdoção){
        return petAdoçãoRepository.save(cadastroPetAdoção);
    }

    public List<CadastroPetAdocao> listarTodos(){
        return petAdoçãoRepository.findAll();
    }

    public CadastroPetAdocao buscarPorId(Long id){
        return petAdoçãoRepository.findById(id).orElse(null);
    }

    public CadastroPetAdocao atualizar(Long id, CadastroPetAdocao cadastroPetAdoçãoAtualizado) {
        CadastroPetAdocao cadastroPetAdoção = petAdoçãoRepository.findById(id).orElse(null);
        if (cadastroPetAdoção != null) {
            cadastroPetAdoção.setRaca(cadastroPetAdoçãoAtualizado.getRaca());
            cadastroPetAdoção.setCor(cadastroPetAdoçãoAtualizado.getCor());
            cadastroPetAdoção.setPorte((cadastroPetAdoçãoAtualizado.getPorte()));
            cadastroPetAdoção.setEstado(cadastroPetAdoçãoAtualizado.getEstado());
            cadastroPetAdoção.setCidade(cadastroPetAdoção.getCidade());
            cadastroPetAdoção.setBairro(cadastroPetAdoçãoAtualizado.getBairro());
            cadastroPetAdoção.setTelefone(cadastroPetAdoçãoAtualizado.getEmail());
            cadastroPetAdoção.setEmail(cadastroPetAdoçãoAtualizado.getTelefone());
            return petAdoçãoRepository.save(cadastroPetAdoção);

        }
        return null;
    }

    public void deletarPorId(Long id){
        petAdoçãoRepository.deleteById(id);
    }

}

