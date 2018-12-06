package controle.gerenciar;

import modelo.Acao;

public class ExportarAcaoTableView {
    private boolean selected;
    private Acao acao;

    public ExportarAcaoTableView() {
    }

    public ExportarAcaoTableView(boolean selected, Acao acao) {
        this.selected = selected;
        this.acao = acao;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }
    
}
