package AchePetWebSite.AchePet.Dto;

import AchePetWebSite.AchePet.Model.PetPerdido;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PetPerdidoResponse {

    private Long cdIdPetPerdido;
    private String nmEspecie;
    private String nmRaca;
    private String dsPorte;
    private String dsCor;
    private String nmPet;
    private String dsIdade;
    private String dsDescricao;
    private String dtPerdido; // yyyy-MM-dd
    private String hrPerdido; // HH:mm:ss
    private String nmEstado;
    private String nmCidade;
    private String nmBairro;
    private String cdTelefone;
    private String nmEmail;
    private List<String> imagens = new ArrayList<>();
    private String dsStatus;
    private String dtRegistro;

    public PetPerdidoResponse() {}

    public PetPerdidoResponse(PetPerdido p, List<String> imagensList) {
        this.cdIdPetPerdido = p.getCdIdPetPerdido();
        this.nmEspecie = p.getNmEspecie();
        this.nmRaca = p.getNmRaca();
        this.dsPorte = p.getDsPorte();
        this.dsCor = p.getDsCor();
        this.nmPet = p.getNmPet();
        this.dsIdade = p.getDsIdade();
        this.dsDescricao = p.getDsDescricao();
        if (p.getDtPerdido() != null) {
            this.dtPerdido = p.getDtPerdido().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (p.getHrPerdido() != null) {
            this.hrPerdido = p.getHrPerdido().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        this.nmEstado = p.getNmEstado();
        this.nmCidade = p.getNmCidade();
        this.nmBairro = p.getNmBairro();
        this.cdTelefone = p.getCdTelefone();
        this.nmEmail = p.getNmEmail();
        this.imagens = imagensList != null ? imagensList : new ArrayList<>();
        this.dsStatus = p.getDsStatus();
        if (p.getDtRegistro() != null) {
            this.dtRegistro = p.getDtRegistro().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    // Getters / Setters (omitted here for brevity in this message — add all standard getters/setters)
    // For completeness in your project add the getters and setters for every field above.
    public Long getCdIdPetPerdido() { return cdIdPetPerdido; }
    public void setCdIdPetPerdido(Long cdIdPetPerdido) { this.cdIdPetPerdido = cdIdPetPerdido; }
    public String getNmEspecie() { return nmEspecie; }
    public void setNmEspecie(String nmEspecie) { this.nmEspecie = nmEspecie; }
    public String getNmRaca() { return nmRaca; }
    public void setNmRaca(String nmRaca) { this.nmRaca = nmRaca; }
    public String getDsPorte() { return dsPorte; }
    public void setDsPorte(String dsPorte) { this.dsPorte = dsPorte; }
    public String getDsCor() { return dsCor; }
    public void setDsCor(String dsCor) { this.dsCor = dsCor; }
    public String getNmPet() { return nmPet; }
    public void setNmPet(String nmPet) { this.nmPet = nmPet; }
    public String getDsIdade() { return dsIdade; }
    public void setDsIdade(String dsIdade) { this.dsIdade = dsIdade; }
    public String getDsDescricao() { return dsDescricao; }
    public void setDsDescricao(String dsDescricao) { this.dsDescricao = dsDescricao; }
    public String getDtPerdido() { return dtPerdido; }
    public void setDtPerdido(String dtPerdido) { this.dtPerdido = dtPerdido; }
    public String getHrPerdido() { return hrPerdido; }
    public void setHrPerdido(String hrPerdido) { this.hrPerdido = hrPerdido; }
    public String getNmEstado() { return nmEstado; }
    public void setNmEstado(String nmEstado) { this.nmEstado = nmEstado; }
    public String getNmCidade() { return nmCidade; }
    public void setNmCidade(String nmCidade) { this.nmCidade = nmCidade; }
    public String getNmBairro() { return nmBairro; }
    public void setNmBairro(String nmBairro) { this.nmBairro = nmBairro; }
    public String getCdTelefone() { return cdTelefone; }
    public void setCdTelefone(String cdTelefone) { this.cdTelefone = cdTelefone; }
    public String getNmEmail() { return nmEmail; }
    public void setNmEmail(String nmEmail) { this.nmEmail = nmEmail; }
    public List<String> getImagens() { return imagens; }
    public void setImagens(List<String> imagens) { this.imagens = imagens; }
    public String getDsStatus() { return dsStatus; }
    public void setDsStatus(String dsStatus) { this.dsStatus = dsStatus; }
    public String getDtRegistro() { return dtRegistro; }
    public void setDtRegistro(String dtRegistro) { this.dtRegistro = dtRegistro; }
}
