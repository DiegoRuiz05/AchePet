package AchePetWebSite.AchePet.Model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_id_usuario")
    private Long id;

    @Column(name = "nm_nome", nullable = false, length = 60)
    private String nome;

    @Column(name = "nm_sobrenome", length = 100)
    private String sobrenome;

    @Column(name = "nm_usuario", nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(name = "cd_cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "nm_email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "cd_telefone", length = 20)
    private String telefone;

    @Column(name = "cd_cep", length = 9)
    private String cep;

    @Column(name = "nm_estado", length = 30)
    private String estado;

    @Column(name = "nm_cidade", length = 100)
    private String cidade;

    @Column(name = "nm_bairro", length = 100)
    private String bairro;

    @Column(name = "ds_senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "dt_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    public Usuario() {}

    // Getters e Setters
    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}

