package AchePetWebSite.AchePet.Dto;

import java.util.List;

public class PetAdocaoImagensResponse {
    private Long id;
    private String mensagem;
    private List<String> caminhosImagens;

    public PetAdocaoImagensResponse() {}
    public PetAdocaoImagensResponse(Long id, String mensagem, List<String> caminhosImagens) {
        this.id = id;
        this.mensagem = mensagem;
        this.caminhosImagens = caminhosImagens;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public List<String> getCaminhosImagens() { return caminhosImagens; }
    public void setCaminhosImagens(List<String> caminhosImagens) { this.caminhosImagens = caminhosImagens; }
}
