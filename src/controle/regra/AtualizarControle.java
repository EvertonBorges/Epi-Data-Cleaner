package controle.regra;

import dao.DaoCondicao;
import dao.DaoOperacao;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import modelo.Condicao;
import modelo.Operacao;
import modelo.OperadorLogicoEnum;
import modelo.Regra;
import modelo.TipoDadoEnum;
import org.hibernate.exception.ConstraintViolationException;
import util.JpaUtil;
import util.Utilidades;

public class AtualizarControle implements Initializable {
    
    private final ObservableList<OperadorLogicoEnum> operadoresLogicos = FXCollections.observableArrayList();
    private ObservableList<Operacao> operacoes;
    private List<CondicoesTela> condicoesTelas;
    private final Regra regra;
    
    @FXML
    private TextField txtTitulo;

    @FXML
    private TextArea txtDescricao;
    
    @FXML
    private ComboBox<TipoDadoEnum> cbTipoDado;
    
    @FXML
    private VBox vBoxGrid;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initObservables();
        initComboBox();
        initDatas();
    }

    public AtualizarControle(Regra regra) {
        this.regra = regra;
    }
    
    private void initObservables() {
        // Inicializando operadores lógicos
        operadoresLogicos.add(OperadorLogicoEnum.E);
        operadoresLogicos.add(OperadorLogicoEnum.OU);
    }
    
    private void initComboBox() {
        this.condicoesTelas = new ArrayList<>();
        
        ObservableList<TipoDadoEnum> tiposDados = FXCollections.observableArrayList();
        tiposDados.add(TipoDadoEnum.TEXTUAL);
        tiposDados.add(TipoDadoEnum.INTEIRO);
        tiposDados.add(TipoDadoEnum.FRACIONARIO);
        tiposDados.add(TipoDadoEnum.DATA);
        
        cbTipoDado.setItems(tiposDados);
        cbTipoDado.setValue(TipoDadoEnum.TEXTUAL);
        
        reloadComboBoxes();
    }
    
    private void initDatas() {
        DaoCondicao daoCondicao = new DaoCondicao();
        List<Condicao> condicoes = daoCondicao.findCondicoesByReference(regra);
        
        txtTitulo.setText(regra.getTitulo());
        cbTipoDado.setValue(regra.getTipoDado());
        txtDescricao.setText(regra.getDescricao());
        
        condicoes.forEach(condicao -> {
            if (condicao == condicoes.get(0))
                addRow(true, condicao.getOperadorLogico(), condicao.getOperacao(), condicao.getValorEsperado());
            else
                addRow(false, condicao.getOperadorLogico(), condicao.getOperacao(), condicao.getValorEsperado());
        });
    }
    
    private void reloadComboBoxes() {
        DaoOperacao dao = new DaoOperacao();
        operacoes = FXCollections.observableArrayList(dao.findOperacoesByTipoDado(cbTipoDado.getSelectionModel().getSelectedItem()));
            
        condicoesTelas.forEach((condicao) -> {
            condicao.getCbOperacao().setItems(operacoes);
            if (!operacoes.isEmpty())
                condicao.getCbOperacao().setValue(operacoes.get(0));
        });
    }
    
    private void addRow(boolean isFirst, OperadorLogicoEnum operadorLogico, Operacao operacao, String valor) {
        ComboBox comboBox1 = new ComboBox(operadoresLogicos);
        comboBox1.setMinWidth(Region.USE_COMPUTED_SIZE);
        comboBox1.setPrefWidth(60);
        comboBox1.setMaxWidth(Region.USE_COMPUTED_SIZE);
        comboBox1.setVisible(!isFirst);
        comboBox1.setValue(operadorLogico);
        
        Label label = new Label("Nome do campo");
        label.setWrapText(true);
        label.setAlignment(Pos.TOP_CENTER);
        label.setMinWidth(Region.USE_COMPUTED_SIZE);
        label.setPrefWidth(70);
        label.setMaxWidth(Region.USE_COMPUTED_SIZE);
        
        ComboBox<Operacao> comboBox2 = new ComboBox(operacoes);
        comboBox2.setMinWidth(Region.USE_COMPUTED_SIZE);
        comboBox2.setPrefWidth(135);
        comboBox2.setMaxWidth(Region.USE_COMPUTED_SIZE);
        comboBox2.setPromptText("Selecione");
        comboBox2.setValue(operacao);
        
        TextField textField = new TextField();
        textField.setMinWidth(Region.USE_COMPUTED_SIZE);
        textField.setPrefWidth(100);
        textField.setMaxWidth(Region.USE_COMPUTED_SIZE);
        textField.setPromptText("Valor");
        textField.setText(valor);
        
        Button button = new Button("-");
        button.setMinWidth(Region.USE_COMPUTED_SIZE);
        button.setPrefWidth(30);
        button.setMaxWidth(Region.USE_COMPUTED_SIZE);
        button.setDisable(isFirst);
        
        CondicoesTela condicoesTela = new CondicoesTela(comboBox1, label, comboBox2, textField, button);
        
        condicoesTelas.add(condicoesTela);
        
        HBox hBox = new HBox(condicoesTela.getCbOperadorLogico(),
                             condicoesTela.getLblCampo(),
                             condicoesTela.getCbOperacao(),
                             condicoesTela.getTxtValor(),
                             condicoesTela.getBtnRemover());
        hBox.setMinHeight(40);
        hBox.setPrefHeight(40);
        hBox.setMaxHeight(Region.USE_COMPUTED_SIZE);
        hBox.setMinWidth(400);
        hBox.setPrefWidth(425);
        hBox.setMaxWidth(450);
        hBox.spacingProperty().set(10);
        
        vBoxGrid.getChildren().add(hBox);
        
        comboBox2.setOnAction(event -> {
            Operacao operacaoSelecionada = ((ComboBox<Operacao>) event.getSource()).getSelectionModel().getSelectedItem();
            textField.setDisable(!operacaoSelecionada.isHabilitarValor());
        });
        
        button.setOnAction((ActionEvent event) -> {
            vBoxGrid.getChildren().remove(hBox);
            condicoesTelas.remove(condicoesTela);
            
            if (!condicoesTelas.isEmpty()) {
                CondicoesTela condicaoTela = condicoesTelas.get(0);
                condicaoTela.getCbOperadorLogico().setValue(null);
                condicaoTela.getCbOperadorLogico().setVisible(false);
            }
            
            if (condicoesTelas.size() <= 1) {
                CondicoesTela condicaoTela = condicoesTelas.get(0);
                condicaoTela.getBtnRemover().setDisable(true);
            }
        });
        
        if (condicoesTelas.size() > 1) {
            CondicoesTela condicaoTela = condicoesTelas.get(0);
            condicaoTela.getBtnRemover().setDisable(false);
        }
    }
    
    @FXML
    void btnAddCondicao_OnAction(ActionEvent event) {
        addRow(false, null, null, "");
    }
    
    @FXML
    void cbTipoDado_OnAction(ActionEvent event) {
        reloadComboBoxes();
    }
    
    @FXML
    void btnSalvar_OnAction(ActionEvent event) {
        try{
            if (cbTipoDado.getValue() == null) {
                String tituloErro = "ERRO";
                String headerErro = "ERRO TIPO DE DADO";
                String mensagemErro = "Por favor informar o tipo de dado da Regra de Validação.";
                
                Utilidades.AlertErro(tituloErro, headerErro, headerErro);
                
                cbTipoDado.requestFocus();
                
                throw new Exception(tituloErro + " - " + headerErro + " - " + mensagemErro);
            } else if (txtTitulo.getText().equals("")) {
                String tituloErro = "ERRO";
                String headerErro = "ERRO TÍTULO";
                String mensagemErro = "Por favor informe o título da Regra de Validação.";
                
                Utilidades.AlertErro(tituloErro, headerErro, headerErro);
                
                txtTitulo.requestFocus();
                
                throw new Exception(tituloErro + " - " + headerErro + " - " + mensagemErro);
            } else if (txtDescricao.getText().equals("")) {
                String tituloErro = "ERRO";
                String headerErro = "ERRO DESCRIÇÃO";
                String mensagemErro = "Por favor informe a descrição da Regra de Validação.";
                
                Utilidades.AlertErro(tituloErro, headerErro, headerErro);
                
                txtDescricao.requestFocus();
                
                throw new Exception(tituloErro + " - " + headerErro + " - " + mensagemErro);
            }
            
            regra.setTipoDado(cbTipoDado.getValue());
            regra.setTitulo(txtTitulo.getText());
            regra.setDescricao(txtDescricao.getText());
            
            List<Condicao> condicoesUpdate = new ArrayList<>();
            for (int i = 0; i < condicoesTelas.size(); i++) {
                CondicoesTela condicao = condicoesTelas.get(i);
                
                if (condicao != condicoesTelas.get(0)) {
                    if (condicao.getCbOperadorLogico().getValue() == null) {
                        String tituloErro = "ERRO";
                        String headerErro = "ERRO OPERADOR LÓGICO";
                        String mensagemErro = "Por favor informe o operador lógico da Regra de Validação.";

                        Utilidades.AlertErro(tituloErro, headerErro, headerErro);

                        condicao.getCbOperadorLogico().requestFocus();

                        throw new Exception(tituloErro + " - " + headerErro + " - " + mensagemErro);
                    }
                }
                
                if (condicao.getCbOperacao().getValue() == null) {
                    String tituloErro = "ERRO";
                    String headerErro = "ERRO OPERAÇÃO";
                    String mensagemErro = "Por favor informe a operação da Regra de Validação.";

                    Utilidades.AlertErro(tituloErro, headerErro, headerErro);

                    condicao.getCbOperacao().requestFocus();

                    throw new Exception(tituloErro + " - " + headerErro + " - " + mensagemErro);
                }
                
                condicoesUpdate.add(new Condicao(condicao.getCbOperadorLogico().getValue(), condicao.getCbOperacao().getValue(), condicao.getTxtValor().getText(), (short) (i + 1), regra));
            }
            
            try {
                DaoCondicao daoCondicao = new DaoCondicao();
                
                daoCondicao.update(regra, condicoesUpdate);
                
                Utilidades.AlertSucesso("SUCESSO", "REGRA CADASTRADA", "Regra de validação ATUALIZADA com sucesso.");

                btnVoltar_OnAction(event);
            } catch(IllegalArgumentException | IllegalStateException | RollbackException | TransactionRequiredException | ConstraintViolationException ex) {
                Utilidades.AlertErro("ERRO", "ERRO DE CADASTRO", "Erro ao tentar ATUALIZAR o cadastro da Regra de Validação.\n\nErro: " + ex.getMessage());
                
                JpaUtil.getEntityManager().getTransaction().rollback();
                throw new Exception(ex.getMessage());
            }
        } catch (Exception ex) {
            // Lançado algum erro genérico.
        }
    }

    @FXML
    void btnVoltar_OnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/regra/Detalhes.fxml"));
        loader.setController(new DetalhesControle(regra));
        
        Stage stage = PerquisarControle.getStage();
        
        stage.setScene(new Scene(loader.load()));
        stage.setMaximized(Utilidades.isMAXIMAZED());
        Utilidades.SetStageDates(stage, "Epi Data Cleaner - Detalhes Regra de Validação");
        stage.show();
    }
    
}