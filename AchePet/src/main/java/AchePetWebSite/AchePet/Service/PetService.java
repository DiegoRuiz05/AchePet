package AchePetWebSite.AchePet.Service;

import AchePetWebSite.AchePet.Model.CadastroPetAdocao;
import AchePetWebSite.AchePet.Repository.CadastroPetAdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private CadastroPetAdocaoRepository petAdocaoRepository;

    public CadastroPetAdocao salvar(CadastroPetAdocao cadastroPetAdocao) {
        return petAdocaoRepository.save(cadastroPetAdocao);
    }

    public List<CadastroPetAdocao> listarTodos() {
        return petAdocaoRepository.findAll();
    }

    public CadastroPetAdocao buscarPorId(Long id) {
        return petAdocaoRepository.findById(id).orElse(null);
    }

    public CadastroPetAdocao atualizar(Long id, CadastroPetAdocao atualizado) {
        CadastroPetAdocao existente = petAdocaoRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNomePet(atualizado.getNomePet());
            existente.setTipo(atualizado.getTipo());
            existente.setRaca(atualizado.getRaca());
            existente.setCor(atualizado.getCor());
            existente.setPorte(atualizado.getPorte());
            existente.setEstado(atualizado.getEstado());
            existente.setCidade(atualizado.getCidade());
            existente.setBairro(atualizado.getBairro());
            existente.setTelefone(atualizado.getTelefone());
            existente.setEmail(atualizado.getEmail());
            existente.setDescricao(atualizado.getDescricao());
            existente.setImagemUrl(atualizado.getImagemUrl());
            return petAdocaoRepository.save(existente);
        }
        return null;
    }


    public void deletarPorId(Long id) {
        petAdocaoRepository.deleteById(id);
    }
}//service do pet
