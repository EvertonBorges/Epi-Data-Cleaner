package modelo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Campo implements Serializable, Cloneable {
    @Id
    @GeneratedValue
    @Column (name = "camid")
    private Long id;
    
    @Column (name = "camnome", nullable = false)
    private String nome;
    
    @Enumerated (EnumType.ORDINAL)
    @Column (name = "camtipodado")
    private TipoDadoEnum tipoDado;
    
    @ManyToOne (optional = false)
    private Arquivo arquivo;
    
    @ManyToMany (mappedBy = "campos")
    private List<Regra> regras;
    
    @OneToMany (mappedBy = "campo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    private List<Acao> acoes;

    public Campo() {
        this.regras = FXCollections.observableArrayList();
        this.acoes = FXCollections.observableArrayList();
    }

    public Campo(String nome, TipoDadoEnum tipoDado) {
        this.nome = nome;
        this.tipoDado = tipoDado;
        this.regras = FXCollections.observableArrayList();
        this.acoes = FXCollections.observableArrayList();
    }

    public Campo(String nome, TipoDadoEnum tipoDado, List<Regra> regras, List<Acao> acoes) {
        this.nome = nome;
        this.tipoDado = tipoDado;
        this.regras = regras;
        this.acoes = acoes;
    }

    public Campo(String nome, TipoDadoEnum tipoDado, Arquivo arquivo, List<Regra> regras, List<Acao> acoes) {
        this.nome = nome;
        this.tipoDado = tipoDado;
        this.arquivo = arquivo;
        this.regras = regras;
        this.acoes = acoes;
    }

    public Campo(Long id, String nome, TipoDadoEnum tipoDado) {
        this.id = id;
        this.nome = nome;
        this.tipoDado = tipoDado;
        this.regras = FXCollections.observableArrayList();
        this.acoes = FXCollections.observableArrayList();
    }

    public Campo(Long id, String nome, TipoDadoEnum tipoDado, List<Regra> regras, List<Acao> acoes) {
        this.id = id;
        this.nome = nome;
        this.tipoDado = tipoDado;
        this.regras = regras;
        this.acoes = acoes;
    }

    public Campo(Long id, String nome, TipoDadoEnum tipoDado, Arquivo arquivo, List<Regra> regras, List<Acao> acoes) {
        this.id = id;
        this.nome = nome;
        this.tipoDado = tipoDado;
        this.arquivo = arquivo;
        this.regras = regras;
        this.acoes = acoes;
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

    public TipoDadoEnum getTipoDado() {
        return tipoDado;
    }

    public void setTipoDado(TipoDadoEnum tipoDado) {
        this.tipoDado = tipoDado;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

    public List<Regra> getRegras() {
        return regras;
    }

    public void setRegras(List<Regra> regras) {
        this.regras = regras;
    }

    public List<Acao> getAcoes() {
        return acoes;
    }

    public void setAcoes(List<Acao> acoes) {
        this.acoes = acoes;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.nome);
        hash = 29 * hash + Objects.hashCode(this.tipoDado);
        hash = 29 * hash + Objects.hashCode(this.arquivo);
        hash = 29 * hash + Objects.hashCode(this.regras);
        hash = 29 * hash + Objects.hashCode(this.acoes);
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
        final Campo other = (Campo) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (this.tipoDado != other.tipoDado) {
            return false;
        }
        if (!Objects.equals(this.arquivo, other.arquivo)) {
            return false;
        }
        if (!this.regras.containsAll(other.regras)) {
            return false;
        }
        return this.acoes.containsAll(other.acoes);
    }

    @Override
    public String toString() {
        return "Campo{" + "nome=" + nome + ", tipoDado=" + tipoDado + ", arquivo=" + arquivo + ", regras=" + regras + ", acoes=" + acoes + '}';
    }

    @Override
    protected Campo clone() throws CloneNotSupportedException {
        return (Campo) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
}