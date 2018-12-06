package modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table (uniqueConstraints = 
            @UniqueConstraint(columnNames = {"opetipodado", "opehabilitarvalor", "openome"}, 
                              name = "uk_operacao"))
public class Operacao implements Serializable, Cloneable {
    @Id
    @GeneratedValue
    @Column (name = "opeid")
    private Long id;
    
    @Enumerated
    @Column (name = "opetipodado", nullable = false)
    private TipoDadoEnum tipoDado;
    @Column (name = "opehabilitarvalor", nullable = false)
    private boolean habilitarValor;
    @Column (length = 25, nullable = false, name = "openome")
    private String nome;

    public Operacao() {
    }

    public Operacao(String nome) {
        this.nome = nome;
    }

    public Operacao(TipoDadoEnum tipoDado, String nome) {
        this.tipoDado = tipoDado;
        this.nome = nome;
    }

    public Operacao(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Operacao(Long id, TipoDadoEnum tipoDado, String nome) {
        this.id = id;
        this.tipoDado = tipoDado;
        this.nome = nome;
    }

    public Operacao(TipoDadoEnum tipoDado, boolean habilitarValor, String nome) {
        this.tipoDado = tipoDado;
        this.habilitarValor = habilitarValor;
        this.nome = nome;
    }

    public Operacao(Long id, TipoDadoEnum tipoDado, boolean habilitarValor, String nome) {
        this.id = id;
        this.tipoDado = tipoDado;
        this.habilitarValor = habilitarValor;
        this.nome = nome;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    public boolean isHabilitarValor() {
        return habilitarValor;
    }

    public void setHabilitarValor(boolean habilitarValor) {
        this.habilitarValor = habilitarValor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Operacao other = (Operacao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    protected Operacao clone() throws CloneNotSupportedException {
        return (Operacao) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
}