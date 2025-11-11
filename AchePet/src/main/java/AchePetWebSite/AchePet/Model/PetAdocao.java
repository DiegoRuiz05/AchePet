package AchePetWebSite.AchePet.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PET_ADOCAO")
public class PetAdocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdIdPetAdocao;

    private String nmEspecie;
    private String nmRaca;
    private String dsPorte;
    private String dsCor;
    private String nmPet;
    private String dsIdade;
    private Boolean icCastrado;
    private Boolean icVacinado;
    private Boolean icDisponivelEntrega;
    @Column(length = 500)
    private String dsDescricao;
    private String nmEstado;
    private String nmCidade;
    private String nmBairro;
    private String cdTelefone;
    private String nmEmail;

    private String dsStatus = "ATIVO";

    private LocalDateTime dtRegistro = LocalDateTime.now();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cd_id_usuario")
//    private Usuario usuario;

    // 🔗 Ligação com o usuário
    @ManyToOne
    @JoinColumn(name = "cd_id_usuario")
    private Usuario usuario;

    // ⚙️ Campo auxiliar para receber o id do usuário no JSON
    @Transient
    private Long cdIdUsuario;

    // getters e setters
    public Long getCdIdPetAdocao() { return cdIdPetAdocao; }
    public void setCdIdPetAdocao(Long cdIdPetAdocao) { this.cdIdPetAdocao = cdIdPetAdocao; }

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

    public LocalDateTime getDtRegistro() { return dtRegistro; }
    public void setDtRegistro(LocalDateTime dtRegistro) { this.dtRegistro = dtRegistro; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Long getCdIdUsuario() {
        return cdIdUsuario;
    }

    public void setCdIdUsuario(Long cdIdUsuario) {
        this.cdIdUsuario = cdIdUsuario;
    }
}
