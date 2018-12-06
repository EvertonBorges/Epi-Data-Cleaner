package controle.gerenciar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.Acao;
import modelo.Regra;
import modelo.TipoDadoEnum;


public class GerirCampo {
    
    private String campo;
    private TipoDadoEnum tipoDado;
    private final ObservableList<Acao> acoesSelecionadasCampo = FXCollections.observableArrayList();
    private final ObservableList<Regra> regrasSelecionadasCampo = FXCollections.observableArrayList();

    public GerirCampo() {
    }
    
    public GerirCampo(String campo) {
        this.campo = campo;
    }

    public GerirCampo(String campo, TipoDadoEnum tipoDado) {
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
        this.tipoDado = tipoDado;
    }

    public ObservableList<Acao> getAcoesSelecionadasCampo() {
        return acoesSelecionadasCampo;
    }

    public ObservableList<Regra> getRegrasSelecionadasCampo() {
        return regrasSelecionadasCampo;
    }
    
}