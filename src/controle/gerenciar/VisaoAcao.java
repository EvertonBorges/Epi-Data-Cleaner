package controle.gerenciar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.Acao;
import modelo.TipoDadoEnum;

public class VisaoAcao {
    
    private String campo;
    private TipoDadoEnum tipoDado;
    private final ObservableList<Acao> acoesSelecionadasCampo = FXCollections.observableArrayList();

    public VisaoAcao() {
    }

    public VisaoAcao(String campo) {
        this.campo = campo;
    }

    public VisaoAcao(String campo, TipoDadoEnum tipoDado) {
        this.campo = campo;
        this.tipoDado = tipoDado;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public TipoDadoEnum getTipoDado() {
        return tipoDado;
    }

    public void setTipoDado(TipoDadoEnum tipoDado) {
        this.acoesSelecionadasCampo.clear();
        this.tipoDado = tipoDado;
    }

    public ObservableList<Acao> getAcoesSelecionadasCampo() {
        return acoesSelecionadasCampo;
    }
    
}
