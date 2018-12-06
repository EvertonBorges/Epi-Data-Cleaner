package modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Condicao implements Serializable, Cloneable {
    @Id
    @GeneratedValue
    @Column (name = "conid")
    private Long id;
    
    @Enumerated
    @Column (name = "conoperadorlogico")
    private OperadorLogicoEnum operadorLogico;
    @ManyToOne (optional = false)
    private Operacao operacao;
    @Column (length = 128, name = "convaloresperado")
    private String valorEsperado;
    
    @Column (name = "conordem", nullable = false)
    private short ordem;
    
    @ManyToOne (optional = false)
    private Regra regra;

    public Condicao() {
        
    }

    public Condicao(OperadorLogicoEnum operadorLogico, String valorEsperado, short ordem) {
        this.operadorLogico = operadorLogico;
        this.valorEsperado = valorEsperado;
        this.ordem = ordem;
    }

    public Condicao(OperadorLogicoEnum operadorLogico, Operacao operacao, String valorEsperado, short ordem) {
        this.operadorLogico = operadorLogico;
        this.operacao = operacao;
        this.valorEsperado = valorEsperado;
        this.ordem = ordem;
    }

    public Condicao(OperadorLogicoEnum operadorLogico, Operacao operacao, String valorEsperado, short ordem, Regra regra) {
        this.operadorLogico = operadorLogico;
        this.operacao = operacao;
        this.valorEsperado = valorEsperado;
        this.ordem = ordem;
        this.regra = regra;
    }

    public Condicao(Long id, OperadorLogicoEnum operadorLogico, String valorEsperado, short ordem) {
        this.id = id;
        this.operadorLogico = operadorLogico;
        this.valorEsperado = valorEsperado;
        this.ordem = ordem;
    }

    public Condicao(Long id, OperadorLogicoEnum operadorLogico, Operacao operacao, String valorEsperado, short ordem, Regra regra) {
        this.id = id;
        this.operadorLogico = operadorLogico;
        this.operacao = operacao;
        this.valorEsperado = valorEsperado;
        this.ordem = ordem;
        this.regra = regra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperadorLogicoEnum getOperadorLogico() {
        return operadorLogico;
    }

    public void setOperadorLogico(OperadorLogicoEnum operadorLogico) {
        this.operadorLogico = operadorLogico;
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public String getValorEsperado() {
        return valorEsperado;
    }

    public void setValorEsperado(String valorEsperado) {
        this.valorEsperado = valorEsperado;
    }

    public short getOrdem() {
        return ordem;
    }

    public void setOrdem(short ordem) {
        this.ordem = ordem;
    }

    public Regra getRegra() {
        return regra;
    }

    public void setRegra(Regra regra) {
        this.regra = regra;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.operadorLogico);
        hash = 61 * hash + Objects.hashCode(this.operacao);
        hash = 61 * hash + Objects.hashCode(this.valorEsperado);
        hash = 61 * hash + this.ordem;
        hash = 61 * hash + Objects.hashCode(this.regra);
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
        final Condicao other = (Condicao) obj;
        if (this.ordem != other.ordem) {
            return false;
        }
        if (!Objects.equals(this.valorEsperado, other.valorEsperado)) {
            return false;
        }
        if (this.operadorLogico != other.operadorLogico) {
            return false;
        }
        if (!Objects.equals(this.operacao, other.operacao)) {
            return false;
        }
        return Objects.equals(this.regra, other.regra);
    }

    @Override
    protected Condicao clone() throws CloneNotSupportedException {
        return (Condicao) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
}