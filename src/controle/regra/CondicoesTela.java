package controle.regra;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.Operacao;
import modelo.OperadorLogicoEnum;

public class CondicoesTela {
    private ComboBox<OperadorLogicoEnum> cbOperadorLogico;
    private Label lblCampo;
    private ComboBox<Operacao> cbOperacao;
    private TextField txtValor;
    private Button btnRemover;

    public CondicoesTela() {
    }

    public CondicoesTela(ComboBox<OperadorLogicoEnum> cbOperadorLogico, Label lblCampo, ComboBox<Operacao> cbOperacao, TextField txtValor, Button btnRemover) {
        this.cbOperadorLogico = cbOperadorLogico;
        this.lblCampo = lblCampo;
        this.cbOperacao = cbOperacao;
        this.txtValor = txtValor;
        this.btnRemover = btnRemover;
    }

    public ComboBox<OperadorLogicoEnum> getCbOperadorLogico() {
        return cbOperadorLogico;
    }

    public void setCbOperadorLogico(ComboBox<OperadorLogicoEnum> cbOperadorLogico) {
        this.cbOperadorLogico = cbOperadorLogico;
    }

    public Label getLblCampo() {
        return lblCampo;
    }

    public void setLblCampo(Label lblCampo) {
        this.lblCampo = lblCampo;
    }

    public ComboBox<Operacao> getCbOperacao() {
        return cbOperacao;
    }

    public void setCbOperacao(ComboBox<Operacao> cbOperacao) {
        this.cbOperacao = cbOperacao;
    }

    public TextField getTxtValor() {
        return txtValor;
    }

    public void setTxtValor(TextField txtValor) {
        this.txtValor = txtValor;
    }

    public Button getBtnRemover() {
        return btnRemover;
    }

    public void setBtnRemover(Button btnRemover) {
        this.btnRemover = btnRemover;
    }
}
