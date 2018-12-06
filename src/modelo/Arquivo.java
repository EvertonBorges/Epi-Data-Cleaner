package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table (uniqueConstraints = 
            @UniqueConstraint(columnNames = {"arqcaminho", "arqnome"}, 
                              name = "uk_arquivo"))
public class Arquivo implements Serializable, Cloneable {
    @Id
    @GeneratedValue
    @Column (name = "arqid")
    private Long id;
    
    @Column (length = 150, nullable = false, name = "arqnome")
    private String nome;
    @Column (length = 512, nullable = false, name = "arqcaminho")
    private String caminho;
    @Column (length = 50, nullable = false, name = "arqdelimitador")
    private String delimitador;
    @Column (nullable = false, name = "arqcabecalho")
    private int cabecalho;
    
    @OneToMany (mappedBy = "arquivo", cascade = CascadeType.ALL)
    private List<Campo> campos;

    public Arquivo() {
        this.campos = new ArrayList<>();
    }

    public Arquivo(String nome, String caminho, String delimitador, int cabecalho) {
        this.nome = nome;
        this.caminho = caminho;
        this.delimitador = delimitador;
        this.cabecalho = cabecalho;
        this.campos = new ArrayList<>();
    }

    public Arquivo(String nome, String caminho, String delimitador, int cabecalho, List<Campo> campos) {
        this.nome = nome;
        this.caminho = caminho;
        this.delimitador = delimitador;
        this.cabecalho = cabecalho;
        this.campos = campos;
    }

    public Arquivo(Long id, String nome, String caminho, String delimitador, int cabecalho) {
        this.id = id;
        this.nome = nome;
        this.caminho = caminho;
        this.delimitador = delimitador;
        this.cabecalho = cabecalho;
        this.campos = new ArrayList<>();
    }

    public Arquivo(Long id, String nome, String caminho, String delimitador, int cabecalho, List<Campo> campos) {
        this.id = id;
        this.nome = nome;
        this.caminho = caminho;
        this.delimitador = delimitador;
        this.cabecalho = cabecalho;
        this.campos = campos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getDelimitador() {
        return delimitador;
    }

    public void setDelimitador(String delimitador) {
        this.delimitador = delimitador;
    }

    public int getCabecalho() {
        return cabecalho;
    }

    public void setCabecalho(int posicaoCabecalho) {
        this.cabecalho = posicaoCabecalho;
    }

    public List<Campo> getCampos() {
        return campos;
    }

    public void setCampos(List<Campo> campos) {
        this.campos = campos;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.nome);
        hash = 89 * hash + Objects.hashCode(this.caminho);
        hash = 89 * hash + Objects.hashCode(this.delimitador);
        hash = 89 * hash + this.cabecalho;
        hash = 89 * hash + Objects.hashCode(this.campos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Arquivo other = (Arquivo) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return Objects.equals(this.caminho, other.caminho);
    }

    @Override
    public Arquivo clone() throws CloneNotSupportedException {
        return (Arquivo) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
}