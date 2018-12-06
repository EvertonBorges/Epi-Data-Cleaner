package modelo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table (uniqueConstraints = 
            @UniqueConstraint(columnNames = {"regtipodado", "regtitulo"}, 
                              name = "uk_regra"))
public class Regra implements Serializable, Cloneable {
    @Id
    @GeneratedValue
    @Column (name = "regid")
    private Long id;
    
    @Enumerated
    @Column (name = "regtipodado", nullable = false)
    private TipoDadoEnum tipoDado;
    
    @Column (length = 100, nullable = false, name = "regtitulo")
    private String titulo;
    @Column (length = 512, nullable = false, name = "regdescricao")
    private String descricao;
    
    @ManyToMany
    private List<Campo> campos;
    
    @OneToMany (mappedBy = "regra", cascade = CascadeType.ALL)
    private List<Condicao> condicoes;
    
    @ManyToMany
    private List<Template> templates;

    public Regra() {
        this.campos = FXCollections.observableArrayList();
        this.condicoes = FXCollections.observableArrayList();
    }

    public Regra(TipoDadoEnum tipoDado, String titulo, String descricao) {
        this.tipoDado = tipoDado;
        this.titulo = titulo;
        this.descricao = descricao;
        this.campos = FXCollections.observableArrayList();
        this.condicoes = FXCollections.observableArrayList();
    }

    public Regra(TipoDadoEnum tipoDado, String titulo, String descricao, List<Campo> campos, List<Condicao> condicoes, List<Template> templates) {
        this.tipoDado = tipoDado;
        this.titulo = titulo;
        this.descricao = descricao;
        this.campos = campos;
        this.condicoes = condicoes;
        this.templates = templates;
    }

    public Regra(Long id, TipoDadoEnum tipoDado, String titulo, String descricao) {
        this.id = id;
        this.tipoDado = tipoDado;
        this.titulo = titulo;
        this.descricao = descricao;
        this.campos = FXCollections.observableArrayList();
        this.condicoes = FXCollections.observableArrayList();
    }

    public Regra(Long id, TipoDadoEnum tipoDado, String titulo, String descricao, List<Campo> campos, List<Condicao> condicoes, List<Template> templates) {
        this.id = id;
        this.tipoDado = tipoDado;
        this.titulo = titulo;
        this.descricao = descricao;
        this.campos = campos;
        this.condicoes = condicoes;
        this.templates = templates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDadoEnum getTipoDado() {
        return tipoDado;
    }

    public void setTipoDado(TipoDadoEnum tipoDado) {
        this.tipoDado = tipoDado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Campo> getCampos() {
        return campos;
    }

    public void setCampos(List<Campo> campos) {
        this.campos = campos;
    }

    public List<Condicao> getCondicoes() {
        return condicoes;
    }

    public void setCondicoes(List<Condicao> condicoes) {
        this.condicoes = condicoes;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Regra other = (Regra) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + " - " + titulo;
    }

    @Override
    protected Regra clone() throws CloneNotSupportedException {
        return (Regra) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
}