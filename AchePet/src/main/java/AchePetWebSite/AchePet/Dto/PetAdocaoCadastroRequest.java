package AchePetWebSite.AchePet.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PetAdocaoCadastroRequest {

    @NotBlank
    private String nmEspecie;

    private String nmRaca;
    private String dsPorte;
    private String dsCor;
    private String nmPet;
    private String dsIdade;

    @NotNull
    private Boolean icCastrado;

    @NotNull
    private Boolean icVacinado;

    @NotNull
    private Boolean icDisponivelEntrega;

    private String dsDescricao;
    private String nmEstado;
    private String nmCidade;
    private String nmBairro;
    private String cdTelefone;
    private String nmEmail;
    private String dsStatus;

    @NotNull
    private Long cdIdUsuario;

    public PetAdocaoCadastroRequest() {}

    // Getters / Setters
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
    public Boolean getIcCastrado() { return icCastrado; }
    public void setIcCastrado(Boolean icCastrado) { this.icCastrado = icCastrado; }
    public Boolean getIcVacinado() { return icVacinado; }
    public void setIcVacinado(Boolean icVacinado) { this.icVacinado = icVacinado; }
    public Boolean getIcDisponivelEntrega() { return icDisponivelEntrega; }
    public void setIcDisponivelEntrega(Boolean icDisponivelEntrega) { this.icDisponivelEntrega = icDisponivelEntrega; }
    public String getDsDescricao() { return dsDescricao; }
    public void setDsDescricao(String dsDescricao) { this.dsDescricao = dsDescricao; }
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
    public String getDsStatus() { return dsStatus; }
    public void setDsStatus(String dsStatus) { this.dsStatus = dsStatus; }
    public Long getCdIdUsuario() { return cdIdUsuario; }
    public void setCdIdUsuario(Long cdIdUsuario) { this.cdIdUsuario = cdIdUsuario; }
}
