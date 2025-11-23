package AchePetWebSite.AchePet.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequest {

    @NotBlank
    @Size(max = 60)
    private String nmNome;

    @Size(max = 100)
    private String nmSobrenome;

    @NotBlank
    @Size(max = 50)
    private String nmUsuario;

    @NotBlank
    @Size(max = 14)
    private String cdCpf;

    @NotBlank
    @Size(max = 100)
    private String nmEmail;

    @Size(max = 20)
    private String cdTelefone;

    @NotBlank
    @Size(max = 255)
    private String dsSenha;

    public UsuarioRequest() {}

    // Getters / Setters
    public String getNmNome() { return nmNome; }
    public void setNmNome(String nmNome) { this.nmNome = nmNome; }
    public String getNmSobrenome() { return nmSobrenome; }
    public void setNmSobrenome(String nmSobrenome) { this.nmSobrenome = nmSobrenome; }
    public String getNmUsuario() { return nmUsuario; }
    public void setNmUsuario(String nmUsuario) { this.nmUsuario = nmUsuario; }
    public String getCdCpf() { return cdCpf; }
    public void setCdCpf(String cdCpf) { this.cdCpf = cdCpf; }
    public String getNmEmail() { return nmEmail; }
    public void setNmEmail(String nmEmail) { this.nmEmail = nmEmail; }
    public String getCdTelefone() { return cdTelefone; }
    public void setCdTelefone(String cdTelefone) { this.cdTelefone = cdTelefone; }
    public String getDsSenha() { return dsSenha; }
    public void setDsSenha(String dsSenha) { this.dsSenha = dsSenha; }
}
