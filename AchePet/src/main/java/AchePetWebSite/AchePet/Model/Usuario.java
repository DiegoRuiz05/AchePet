package AchePetWebSite.AchePet.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdIdUsuario;

    private String nmNome;

    private String nmSobrenome;

    @Column(unique = true, nullable = false)
    private String nmUsuario;

    @Column(unique = true, nullable = false, length = 14)
    private String cdCpf;

    @Column(unique = true, nullable = false)
    private String nmEmail;

    private String cdTelefone;
//    private String cdCep;
//    private String nmEstado;
//    private String nmCidade;
//    private String nmBairro;

    @Column(nullable = false)
    private String dsSenha;

    private LocalDateTime dtCadastro = LocalDateTime.now();

    // getters e setters
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

//    public String getCdCep() { return cdCep; }
//    public void setCdCep(String cdCep) { this.cdCep = cdCep; }
//
//    public String getNmEstado() { return nmEstado; }
//    public void setNmEstado(String nmEstado) { this.nmEstado = nmEstado; }
//
//    public String getNmCidade() { return nmCidade; }
//    public void setNmCidade(String nmCidade) { this.nmCidade = nmCidade; }
//
//    public String getNmBairro() { return nmBairro; }
//    public void setNmBairro(String nmBairro) { this.nmBairro = nmBairro; }

    public String getDsSenha() { return dsSenha; }
    public void setDsSenha(String dsSenha) { this.dsSenha = dsSenha; }

    public LocalDateTime getDtCadastro() { return dtCadastro; }
    public void setDtCadastro(LocalDateTime dtCadastro) { this.dtCadastro = dtCadastro; }
}
