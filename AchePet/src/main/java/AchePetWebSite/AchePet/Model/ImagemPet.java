package AchePetWebSite.AchePet.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "IMAGEM_PET")
public class ImagemPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;

    private String urlImagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_id_pet_adocao")
    @JsonBackReference
    private PetAdocao petAdocao;


    // Getters e Setters
    public Long getId() { return id; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    public PetAdocao getPetAdocao() { return petAdocao; }
    public void setPetAdocao(PetAdocao petAdocao) { this.petAdocao = petAdocao; }
}

