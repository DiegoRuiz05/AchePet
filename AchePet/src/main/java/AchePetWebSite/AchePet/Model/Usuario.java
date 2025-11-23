package AchePetWebSite.AchePet.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_id_usuario")
    private Long cdIdUsuario;

    @Column(name = "nm_nome", nullable = false, length = 60)
    private String nmNome;

    @Column(name = "nm_sobrenome", length = 100)
    private String nmSobrenome;

    @Column(name = "nm_usuario", nullable = false, unique = true, length = 50)
    private String nmUsuario;

    @Column(name = "cd_cpf", nullable = false, unique = true, length = 14)
    private String cdCpf;

    @Column(name = "nm_email", nullable = false, unique = true, length = 100)
    private String nmEmail;

    @Column(name = "cd_telefone", length = 20)
    private String cdTelefone;

    @Column(name = "ds_senha", nullable = false, length = 255)
    private String dsSenha;

    @Column(name = "dt_cadastro")
    private LocalDateTime dtCadastro = LocalDateTime.now();

    // Relacionamento de 1 usuário para N pets
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetAdocao> pets = new ArrayList<>();

    public Usuario() {}

    // Getters e Setters
    public Long getCdIdUsuario() {
        return cdIdUsuario;
    }

    public void setCdIdUsuario(Long cdIdUsuario) {
        this.cdIdUsuario = cdIdUsuario;
    }

    public String getNmNome() {
        return nmNome;
    }

    public void setNmNome(String nmNome) {
        this.nmNome = nmNome;
    }

    public String getNmSobrenome() {
        return nmSobrenome;
    }

    public void setNmSobrenome(String nmSobrenome) {
        this.nmSobrenome = nmSobrenome;
    }

    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    public String getCdCpf() {
        return cdCpf;
    }

    public void setCdCpf(String cdCpf) {
        this.cdCpf = cdCpf;
    }

    public String getNmEmail() {
        return nmEmail;
    }

    public void setNmEmail(String nmEmail) {
        this.nmEmail = nmEmail;
    }

    public String getCdTelefone() {
        return cdTelefone;
    }

    public void setCdTelefone(String cdTelefone) {
        this.cdTelefone = cdTelefone;
    }

    public String getDsSenha() {
        return dsSenha;
    }

    public void setDsSenha(String dsSenha) {
        this.dsSenha = dsSenha;
    }

    public LocalDateTime getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDateTime dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public List<PetAdocao> getPets() {
        return pets;
    }

    public void setPets(List<PetAdocao> pets) {
        this.pets = pets;
    }

    // Métodos de relacionamento
    public void addPet(PetAdocao pet) {
        if (pet == null) return;
        pet.setUsuario(this);
        this.pets.add(pet);
    }

    public void removePet(PetAdocao pet) {
        if (pet == null) return;
        pet.setUsuario(null);
        this.pets.remove(pet);
    }
}
