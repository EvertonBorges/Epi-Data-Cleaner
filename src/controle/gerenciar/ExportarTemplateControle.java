package controle.gerenciar;

import dao.DaoRegra;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Acao;
import modelo.Arquivo;
import modelo.Regra;
import modelo.TipoDadoEnum;
import util.Utilidades;

public class ExportarTemplateControle implements Initializable {
    
    private final Stage stage;
    private final Arquivo arquivo; // Estrutura contendo as principais informações do arquivo selecionado.
    private final ObservableList<String> tiposDados = FXCollections.observableArrayList(); // Tipos de dados que podem ser selecionados.
    private final ObservableList<Regra> regrasTodas; // Todas as regras sem exceção.
    private final ObservableList<Regra> regrasSelecionadasSessao; // Todas as regras que foram marcadas na tela do gerenciamento de campos.
    private final ObservableList<Regra> regrasMostrar = FXCollections.observableArrayList();
    private final List<Regra> regrasSelecionadas = new ArrayList<>();
    //private final List<ExportarAcaoTableView> acoesSelecionadas = new ArrayList<>();
    //private final ObservableList<ExportarAcaoTableView> acoesCampo = FXCollections.observableArrayList();
    //private final ToggleGroup tgCampos = new ToggleGroup();
    //private int indiceCampoSelecionado;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initTiposDados();
        initCampos();
        initTableView();
    }

    public ExportarTemplateControle(Stage stage, Arquivo arquivo) {
        this.stage = stage;
        this.arquivo = arquivo;
        this.regrasSelecionadasSessao = FXCollections.observableArrayList();
        
        // Inicializa a lista com todas as regras
        this.arquivo.getCampos().forEach(manageView -> {
            manageView.getRegras().forEach(regra -> {
                if (!regrasSelecionadasSessao.contains(regra)) {
                    regrasSelecionadasSessao.add(regra);
                }
            });
        });
        
        this.regrasTodas = FXCollections.observableList(new DaoRegra().findRegras("titulo", ""));
    }
    
    @FXML
    private ComboBox<String> cbTipoDado;
    
    @FXML
    private VBox vBoxRegras;
    
    @FXML
    private CheckBox chkMostrarNaoUtilizadas;

    @FXML
    void btnExportarTemplate_OnAction(ActionEvent event) {
        
    }
    
    @FXML
    void btnVoltar_OnAction(ActionEvent event) {
        GerenciarCamposControle.getStage().show();
        stage.close();
    }

    @FXML
    void cbTipoDado_OnAction(ActionEvent event) {
        vBoxRegras.getChildren().clear();
        
        ObservableList<Regra> lista;
            
        if (chkMostrarNaoUtilizadas.isSelected()) {
            lista = regrasTodas;
        } else {
            lista = regrasSelecionadasSessao;
        }
        
        if ((cbTipoDado.getSelectionModel().getSelectedIndex() == 0) || (cbTipoDado.getSelectionModel().getSelectedItem() == null)) {
            lista.forEach(regra -> {
                CheckBox checkBox = new CheckBox(regra.toString());
                checkBox.setMinHeight(25);
                checkBox.setPrefHeight(25);
                checkBox.setMaxHeight(25);
                
                if (regrasSelecionadas.contains(regra)) {
                    checkBox.setSelected(true);
                } else {
                    checkBox.setSelected(false);
                }

                checkBox.setOnAction(evento -> {
                    // Identifica se está selecionando ou desselecionando.
                    boolean isSelect = ((CheckBox) evento.getSource()).isSelected();

                    // Pega a referência da regra que está sendo selecionada ou desselecionado.
                    Regra regraSelecionada = lista.get(vBoxRegras.getChildren().indexOf(evento.getSource()));

                    // Adiciona ou remove a regra da lista das selecionadas.
                    if (isSelect && !regrasSelecionadas.contains(regraSelecionada)) {
                        regrasSelecionadas.add(regra);
                    } else if (!isSelect && regrasSelecionadas.contains(regraSelecionada)) {
                        regrasSelecionadas.remove(regra);
                    }
                });
                
                vBoxRegras.getChildren().add(checkBox);
            });
        } else {
            try {
                TipoDadoEnum tipoDado = TipoDadoEnum.valueOf(cbTipoDado.getSelectionModel().getSelectedItem());

                lista.forEach(regra -> {
                    if (regra.getTipoDado() == tipoDado) {
                        CheckBox checkBox = new CheckBox(regra.toString());
                        checkBox.setMinHeight(25);
                        checkBox.setPrefHeight(25);
                        checkBox.setMaxHeight(25);
                        
                        if (regrasSelecionadas.contains(regra)) {
                            checkBox.setSelected(true);
                        } else {
                            checkBox.setSelected(false);
                        }

                        checkBox.setOnAction(evento -> {
                            // Identifica se está selecionando ou desselecionando.
                            boolean isSelect = ((CheckBox) evento.getSource()).isSelected();

                            // Pega a referência da regra que está sendo selecionada ou desselecionado.
                            Regra regraSelecionada = lista.get(vBoxRegras.getChildren().indexOf(evento.getSource()));

                            // Adiciona ou remove a regra da lista das selecionadas.
                            if (isSelect && !regrasSelecionadas.contains(regraSelecionada)) {
                                regrasSelecionadas.add(regra);
                            } else if (!isSelect && regrasSelecionadas.contains(regraSelecionada)) {
                                regrasSelecionadas.remove(regra);
                            }
                        });

                        vBoxRegras.getChildren().add(checkBox);
                    }
                });
            } catch (IllegalArgumentException ex) {
                Utilidades.AlertErro("ERRO", "ERRO INESPERADO", "Houve um erro inesperado.");
            }
        }
    }

    @FXML
    void chkSelecionarTodasRegras_OnAction(ActionEvent event) {
        
    }
    
    @FXML
    void chkMostrarNaoUtilizadas_OnAction(ActionEvent event) {
        cbTipoDado_OnAction(event);
    }

    @FXML
    void miExportarTemplate_OnAction(ActionEvent event) {
        
    }

    @FXML
    void miSair_OnAction(ActionEvent event) {
        btnVoltar_OnAction(event);
    }

    @FXML
    void miSobre_OnAction(ActionEvent event) {
        
    }
    
    private void initTiposDados() {
        cbTipoDado.setItems(tiposDados);
        
        tiposDados.clear();
        tiposDados.add("Selecione");
        for (TipoDadoEnum tipoDado: TipoDadoEnum.values()) {
            tiposDados.add(tipoDado.name());
        }
    }
    
    private void initCampos() {
        /*        
        acoesSelecionadas.clear();
        manageViews.forEach(manageView -> {
            manageView.getAcoesSelecionadasCampo().forEach(acao -> {
                acoesSelecionadas.add(new ExportarAcaoTableView(false, acao));
            });
        });
        
        // Evento para os toggle buttons
        tgCampos.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (!tgCampos.getToggles().isEmpty()) {
                acoesCampo.clear();
                if (tgCampos.getToggles().get(0).equals(newValue)) {
                    acoesCampo.addAll(acoesSelecionadas);
                } else {
                    int indice = tgCampos.getToggles().indexOf(newValue);
                    
                    if (indice > 0) {
                        GerirCampo manageView = manageViews.get(indice - 1);
                        manageView.getAcoesSelecionadasCampo().forEach(acao -> {
                            ExportarAcaoTableView acaoTableView = encontraAcaoTableView(acao);
                            if (acaoTableView != null) acoesCampo.add(acaoTableView);
                        });
                    }
                }
            }
        });
        
        ToggleButton btnTodos = new ToggleButton("TODOS");
            
        btnTodos.setMinSize(40, 25);
        btnTodos.setMaxSize(150, 25);
        btnTodos.autosize();
        btnTodos.setToggleGroup(tgCampos);
        
        hBoxCampos.getChildren().add(btnTodos);
        
        arquivo.getCampos().forEach(campo -> {
            final String fieldName = campo.getNome();
            
            ToggleButton btnToggle = new ToggleButton(fieldName.toUpperCase());
            
            btnToggle.setMinSize(40, 25);
            btnToggle.setMaxSize(150, 25);
            btnToggle.autosize();
            btnToggle.setToggleGroup(tgCampos);
            
            hBoxCampos.getChildren().add(btnToggle);
        });
        
        // Força a seleção do primeiro Toggle na abertura da janela.
        if (!tgCampos.getToggles().isEmpty()) {
            ToggleButton button = (ToggleButton) tgCampos.getToggles().get(0);

            button.setSelected(true);
        }
        */
    }
    
    private void initTableView() {
        /*
        tvAcoes.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tvAcoes.setItems(acoesCampo);

        tcExportar.setCellValueFactory(new PropertyValueFactory<>("selected"));
        tcCampo.setCellValueFactory((param) -> new SimpleStringProperty(param.getValue().getAcao().getCampo().getNome()));
        tcAcaoOrdem.setCellValueFactory((param) -> new SimpleStringProperty(String.valueOf(param.getValue().getAcao().getOrdem())));
        tcAcaoCondicao.setCellValueFactory((param) -> new SimpleStringProperty(param.getValue().getAcao().getOperacao().getNome()));
        tcAcaoValor.setCellValueFactory((param) -> new SimpleStringProperty(param.getValue().getAcao().getValor()));
        tcAcaoNovoValor.setCellValueFactory((param) -> new SimpleStringProperty(param.getValue().getAcao().getNovoValor()));

        tcExportar.setCellFactory(CheckBoxTableCell.forTableColumn(tcExportar));
        */
    }
    
    /*
    private ExportarAcaoTableView encontraAcaoTableView(Acao acao) {
        for (ExportarAcaoTableView acaoSelecionada: acoesSelecionadas) {
            if (acaoSelecionada.getAcao().equals(acao)) {
                return acaoSelecionada;
            }
        }
        
        return null;
    }
    */
    
}
