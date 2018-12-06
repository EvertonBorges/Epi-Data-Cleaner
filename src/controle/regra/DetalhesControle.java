package controle.regra;

import dao.DaoCondicao;
import dao.DaoRegra;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import modelo.Condicao;
import modelo.Regra;
import org.hibernate.exception.ConstraintViolationException;
import util.Utilidades;

public class DetalhesControle implements Initializable {
    
    private final Regra regra;
    private List<Condicao> condicoes;
    
    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblTipoDado;
    
    @FXML
    private Label lblDescricao;

    @FXML
    private Label lblCondicoes;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDatas();
    }

    public DetalhesControle(Regra regra) {
        this.regra = regra;
    }
    
    private void loadDatas() {
        DaoCondicao daoCondicao = new DaoCondicao();
        condicoes = daoCondicao.findCondicoesByReference(regra);
        
        lblTitulo.setText("\t" + regra.getTitulo());
        lblTipoDado.setText("\t" + regra.getTipoDado().toString());
        lblDescricao.setText("\t" + regra.getDescricao());
        lblCondicoes.setText(Utilidades.concatConditions(condicoes));
    }
    
    @FXML
    void btnAtualizar_OnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/regra/Atualizar.fxml"));
        loader.setController(new AtualizarControle(regra));
        
        Stage stage = PerquisarControle.getStage();
        
        stage.setScene(new Scene(loader.load()));
        stage.setMaximized(Utilidades.isMAXIMAZED());
        Utilidades.SetStageDates(stage, "Epi Data Cleaner - Atualizar Regra de Validação");
        stage.show();
    }

    @FXML
    void btnRemover_OnAction(ActionEvent event) {
        Alert alert = Utilidades.AlertYesNo("AVISO", "REMOVER REGRA DE VALIDAÇÃO", "Deseja realmente remover a regra de validação '" + regra.getTitulo() + "' ?");
        
        alert.showAndWait().ifPresent(button -> {
            if (button == ButtonType.YES) {
                try {
                    DaoRegra daoRegra = new DaoRegra();
                    DaoCondicao daoCondicao = new DaoCondicao();

                    daoCondicao.remove(regra);
                    daoRegra.remove(regra);
                    
                    Utilidades.AlertInformation("AVISO", "SUCESSO", "Regra de Validação removida com SUCESSO");

                    btnVoltar_OnAction(event);
                } catch (IOException | IllegalArgumentException | IllegalStateException | RollbackException | TransactionRequiredException | ConstraintViolationException ex) {
                    System.out.println("Erro: " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    void btnVoltar_OnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/regra/Pesquisar.fxml"));
        loader.setController(new PerquisarControle());
        Stage stage = PerquisarControle.getStage();
        
        stage.setScene(new Scene(loader.load()));
        stage.setMaximized(Utilidades.isMAXIMAZED());
        Utilidades.SetStageDates(stage, "Epi Data Cleaner - Pesquisar Regras de Validações");
        stage.show();
    }
    
}
