package modelo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Template implements Serializable, Cloneable {
    @Id
    @GeneratedValue
    @Column (name = "temid")
    private Long id;
    
    @Column (name = "temnome", nullable = false)
    private String nome;
    
    @ManyToMany (mappedBy = "templates", cascade = CascadeType.ALL)
    private List<Regra> regras;

    public Template() {
        this.regras = FXCollections.observableArrayList();
    }

    public Template(String nome) {
        this.nome = nome;
        this.regras = FXCollections.observableArrayList();
    }

    public Template(String nome, List<Regra> regras) {
        this.nome = nome;
        this.regras = regras;
    }

    public Template(Long id, String nome) {
        this.id = id;
        this.nome = nome;
        this.regras = FXCollections.observableArrayList();
    }

    public Template(Long id, String nome, List<Regra> regras) {
        this.id = id;
        this.nome = nome;
        this.regras = regras;
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

    public List<Regra> getRegras() {
        return regras;
    }

    public void setRegras(List<Regra> regras) {
        this.regras = regras;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.nome);
        hash = 17 * hash + Objects.hashCode(this.regras);
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
        final Template other = (Template) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return this.regras.containsAll(other.regras);
    }

    @Override
    protected Template clone() throws CloneNotSupportedException {
        return (Template) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
}