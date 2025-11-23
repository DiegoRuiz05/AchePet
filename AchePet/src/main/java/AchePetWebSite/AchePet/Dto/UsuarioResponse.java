package AchePetWebSite.AchePet.Dto;

import AchePetWebSite.AchePet.Model.Usuario;

import java.time.format.DateTimeFormatter;

public class UsuarioResponse {

    private Long cdIdUsuario;
    private String nmNome;
    private String nmSobrenome;
    private String nmUsuario;
    private String cdCpf;
    private String nmEmail;
    private String cdTelefone;
    private String dtCadastro; // formatado como String

    public UsuarioResponse() {}

    public UsuarioResponse(Usuario u) {
        this.cdIdUsuario = u.getCdIdUsuario();
        this.nmNome = u.getNmNome();
        this.nmSobrenome = u.getNmSobrenome();
        this.nmUsuario = u.getNmUsuario();
        this.cdCpf = u.getCdCpf();
        this.nmEmail = u.getNmEmail();
        this.cdTelefone = u.getCdTelefone();
        if (u.getDtCadastro() != null) {
            this.dtCadastro = u.getDtCadastro().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    // Getters / Setters
    public Long getCdIdUsuario() { return cdIdUsuario; }
    public void setCdIdUsuario(Long cdIdUsuario) { this.cdIdUsuario = cdIdUsuario; }
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
    public String getDtCadastro() { return dtCadastro; }
    public void setDtCadastro(String dtCadastro) { this.dtCadastro = dtCadastro; }
}
