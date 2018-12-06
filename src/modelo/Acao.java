package modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Acao implements Serializable, Cloneable {
    @Id
    @GeneratedValue
    @Column (name = "acaid")
    private Long id;
    
    @ManyToOne (optional = false)
    private Operacao operacao;
    
    @Column (name = "acavalor")
    private String valor;
    @Column (name = "acanovovalor", nullable = false)
    private String novoValor;
    
    @Column (name = "acaordem", nullable = false)
    private short ordem;
    
    @ManyToOne (optional = false)
    private Campo campo;

    public Acao() {
    }

    public Acao(Operacao operacao, String valor, String novoValor, short ordem) {
        this.operacao = operacao;
        this.valor = valor;
        this.novoValor = novoValor;
        this.ordem = ordem;
    }

    public Acao(Operacao operacao, String valor, String novoValor, short ordem, Campo campo) {
        this.operacao = operacao;
        this.valor = valor;
        this.novoValor = novoValor;
        this.ordem = ordem;
        this.campo = campo;
    }

    public Acao(Long id, Operacao operacao, String valor, String novoValor, short ordem) {
        this.id = id;
        this.operacao = operacao;
        this.valor = valor;
        this.novoValor = novoValor;
        this.ordem = ordem;
    }

    public Acao(Long id, Operacao operacao, String valor, String novoValor, short ordem, Campo campo) {
        this.id = id;
        this.operacao = operacao;
        this.valor = valor;
        this.novoValor = novoValor;
        this.ordem = ordem;
        this.campo = campo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getNovoValor() {
        return novoValor;
    }

    public void setNovoValor(String novoValor) {
        this.novoValor = novoValor;
    }

    public short getOrdem() {
        return ordem;
    }

    public void setOrdem(short ordem) {
        this.ordem = ordem;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.operacao);
        hash = 97 * hash + Objects.hashCode(this.valor);
        hash = 97 * hash + Objects.hashCode(this.novoValor);
        hash = 97 * hash + this.ordem;
        hash = 97 * hash + Objects.hashCode(this.campo);
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
        final Acao other = (Acao) obj;
        if (this.ordem != other.ordem) {
            return false;
        }
        if (!Objects.equals(this.valor, other.valor)) {
            return false;
        }
        if (!Objects.equals(this.novoValor, other.novoValor)) {
            return false;
        }
        return Objects.equals(this.operacao, other.operacao);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
}