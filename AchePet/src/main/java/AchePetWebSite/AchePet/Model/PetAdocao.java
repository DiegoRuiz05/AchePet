package AchePetWebSite.AchePet.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PET_ADOCAO")
public class PetAdocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_id_pet_adocao")
    private Long cdIdPetAdocao;

    @Column(name = "nm_especie", nullable = false, length = 50)
    private String nmEspecie;

    @Column(name = "nm_raca", length = 100)
    private String nmRaca;

    @Column(name = "ds_porte", length = 30)
    private String dsPorte;

    @Column(name = "ds_cor", length = 50)
    private String dsCor;

    @Column(name = "nm_pet", length = 100)
    private String nmPet;

    @Column(name = "ds_idade", length = 30)
    private String dsIdade;

    @Column(name = "ic_castrado")
    private Boolean icCastrado;

    @Column(name = "ic_vacinado")
    private Boolean icVacinado;

    @Column(name = "ic_disponivel_entrega")
    private Boolean icDisponivelEntrega;

    @Column(name = "ds_descricao", length = 80)
    private String dsDescricao;

    @Column(name = "nm_estado", length = 30)
    private String nmEstado;

    @Column(name = "nm_cidade", length = 100)
    private String nmCidade;

    @Column(name = "nm_bairro", length = 100)
    private String nmBairro;

    @Column(name = "cd_telefone", length = 20)
    private String cdTelefone;

    @Column(name = "nm_email", length = 100)
    private String nmEmail;

    // Campo JSON contendo a lista de caminhos de imagens
    @Column(name = "ds_caminho_imagem", columnDefinition = "JSON")
    private String dsCaminhoImagem;

    // Status do pet: ATIVO ou INATIVO
    @Column(name = "ds_status", columnDefinition = "ENUM('ATIVO','INATIVO') default 'ATIVO'")
    private String dsStatus = "ATIVO";

    @Column(name = "dt_registro")
    private LocalDateTime dtRegistro = LocalDateTime.now();

    // Relacionamento com USUARIO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_id_usuario")
    private Usuario usuario;

    public PetAdocao() {}

    // Getter e Setter
    public Long getCdIdPetAdocao() {
        return cdIdPetAdocao;
    }

    public void setCdIdPetAdocao(Long cdIdPetAdocao) {
        this.cdIdPetAdocao = cdIdPetAdocao;
    }

    public String getNmEspecie() {
        return nmEspecie;
    }

    public void setNmEspecie(String nmEspecie) {
        this.nmEspecie = nmEspecie;
    }

    public String getNmRaca() {
        return nmRaca;
    }

    public void setNmRaca(String nmRaca) {
        this.nmRaca = nmRaca;
    }

    public String getDsPorte() {
        return dsPorte;
    }

    public void setDsPorte(String dsPorte) {
        this.dsPorte = dsPorte;
    }

    public String getDsCor() {
        return dsCor;
    }

    public void setDsCor(String dsCor) {
        this.dsCor = dsCor;
    }

    public String getNmPet() {
        return nmPet;
    }

    public void setNmPet(String nmPet) {
        this.nmPet = nmPet;
    }

    public String getDsIdade() {
        return dsIdade;
    }

    public void setDsIdade(String dsIdade) {
        this.dsIdade = dsIdade;
    }

    public Boolean getIcCastrado() {
        return icCastrado;
    }

    public void setIcCastrado(Boolean icCastrado) {
        this.icCastrado = icCastrado;
    }

    public Boolean getIcVacinado() {
        return icVacinado;
    }

    public void setIcVacinado(Boolean icVacinado) {
        this.icVacinado = icVacinado;
    }

    public Boolean getIcDisponivelEntrega() {
        return icDisponivelEntrega;
    }

    public void setIcDisponivelEntrega(Boolean icDisponivelEntrega) {
        this.icDisponivelEntrega = icDisponivelEntrega;
    }

    public String getDsDescricao() {
        return dsDescricao;
    }

    public void setDsDescricao(String dsDescricao) {
        this.dsDescricao = dsDescricao;
    }

    public String getNmEstado() {
        return nmEstado;
    }

    public void setNmEstado(String nmEstado) {
        this.nmEstado = nmEstado;
    }

    public String getNmCidade() {
        return nmCidade;
    }

    public void setNmCidade(String nmCidade) {
        this.nmCidade = nmCidade;
    }

    public String getNmBairro() {
        return nmBairro;
    }

    public void setNmBairro(String nmBairro) {
        this.nmBairro = nmBairro;
    }

    public String getCdTelefone() {
        return cdTelefone;
    }

    public void setCdTelefone(String cdTelefone) {
        this.cdTelefone = cdTelefone;
    }

    public String getNmEmail() {
        return nmEmail;
    }

    public void setNmEmail(String nmEmail) {
        this.nmEmail = nmEmail;
    }

    public String getDsCaminhoImagem() {
        return dsCaminhoImagem;
    }

    public void setDsCaminhoImagem(String dsCaminhoImagem) {
        this.dsCaminhoImagem = dsCaminhoImagem;
    }

    public String getDsStatus() {
        return dsStatus;
    }

    public void setDsStatus(String dsStatus) {
        this.dsStatus = dsStatus;
    }

    public LocalDateTime getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(LocalDateTime dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
