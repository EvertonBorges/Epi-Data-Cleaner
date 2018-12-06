package controle.gerenciar;

import controle.EpiDataCleaner;
import dao.DaoCondicao;
import dao.DaoOperacao;
import dao.DaoRegra;
import controle.regra.PerquisarControle;
import dao.DaoArquivo;
import dao.DaoCampo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modelo.Acao;
import modelo.Arquivo;
import modelo.Campo;
import modelo.Condicao;
import modelo.Operacao;
import modelo.Regra;
import modelo.TipoDadoEnum;
import org.hibernate.Hibernate;
import util.Utilidades;
import util.Validador;

public class GerenciarCamposControle implements Initializable {
    
    private static Stage managerStage;
    
    private final String[][] dados; // Estrutura contendo todos os dados selecionado pelo usuário.
    private final Arquivo arquivo; // Estrutura contendo as principais informações do arquivo selecionado.
    private final ObservableList<String> tiposDados = FXCollections.observableArrayList(); // Tipos de dados que podem ser selecionados.
    private ObservableList<Regra> regrasAtuais; // Regras que mostram na aba Regras de Validações.
    private Campo campoAtual; // ManageView contendo os dados do campo selecionado.
    private final ObservableList<VisaoPrevia> previewViews = FXCollections.observableArrayList(); // Contem todos os resultados encontrados no momento de montar a prévia.
    private final ToggleGroup tgCampos = new ToggleGroup();
    private int indiceCampoSelecionado;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        iniciarComboTipoDados();
        iniciarCampos();
        iniciarEventos();
        iniciarColunasTvPreview();
        iniciarColunasTvActions();
    }

    public GerenciarCamposControle(Stage stage, String[][] dados, Arquivo arquivo) {
        GerenciarCamposControle.managerStage = stage;
        this.dados = dados;
        this.arquivo = arquivo;
        
        carregarArquivo();
    }

    public GerenciarCamposControle(String[][] dados, Arquivo arquivo) {
        this.dados = dados;
        this.arquivo = arquivo;
        
        carregarArquivo();
    }
    
    @FXML
    private HBox hBoxCampos;
    
    @FXML
    private TitledPane tpManage;

    @FXML
    private TitledPane tpPreview;
    
    @FXML
    private ComboBox<String> cbTipoDado;
    
    @FXML
    private VBox vBoxRegrasAtuais;
    
    @FXML
    private Button btnPreview;
    
    @FXML
    private AnchorPane mainAnchorPane;
    
    @FXML
    private TableView<VisaoPrevia> tvPreview;
    
    @FXML
    private Label lblAction;

    @FXML
    private ComboBox<Operacao> cbCondicaoAction;

    @FXML
    private TextField txtValorAction;

    @FXML
    private TextField txtNovoValorAction;

    @FXML
    private TableView<Acao> tvActions;
    
    @FXML
    private Button btnAddAction;
    
    @FXML
    void miRegraValidacao_OnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/regra/Pesquisar.fxml"));
            Stage stage = new Stage();
            loader.setController(new PerquisarControle(stage));
            stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
                Utilidades.setIS_MAXIMAZED(newValue);
            });
            
            Utilidades.SetStageDates(stage, "Epi Data Cleaner - Regras de Validação");
            stage.setScene(new Scene(loader.load()));
            stage.getIcons().add(Utilidades.getIconApplication());
            stage.setMaximized(Utilidades.isMAXIMAZED());
            GerenciarCamposControle.getStage().hide();
            stage.show();
        } catch (IOException ex) {
            Utilidades.AlertErro("ERRO", 
                "ERRO INESPERADO", 
                "Erro ao abrir a janela de \"Gerenciamento de Campos\".\n\nErro:\n" + ex.getMessage() + "\nCausado Por:\n" + ex.getCause().getMessage());
        }
    }
    
    @FXML
    void btnVoltar_OnAction(ActionEvent event) {
        EpiDataCleaner.getStage().show();
        getStage().close();
    }
    
    @FXML
    void btnPreview_OnAction(ActionEvent event) {
        this.previewViews.clear();
        
        try {
            if (dados != null && !tpManage.getText().isEmpty()) {
                final int indexColumn = hBoxCampos.getChildren().indexOf(tgCampos.getSelectedToggle());

                for (int i = 0; i < dados.length; i++) {
                    if (i != arquivo.getCabecalho()) {
                        String columnValue = dados[i][indexColumn];
                        
                        VisaoPrevia previewView = new VisaoPrevia();
                        previewView.setLine(i);
                        previewView.setValue(columnValue);
                        
                        boolean isValidated = false;
                        for (Regra regra: this.arquivo.getCampos().get(indexColumn).getRegras()) {
                            List<Condicao> condicoesRegra = new DaoCondicao().findCondicoesByReference(regra); // Condições da regra
                            List<String[]> expressoes = Utilidades.expressaoMatematica(condicoesRegra, previewView.getValue()); // Molda em forma de expressão
                            expressoes.forEach(expressao -> {
                                System.out.println("Expressão: " + Arrays.toString(expressao));
                            });
                            isValidated = Validador.validacaoOu(campoAtual.getTipoDado(), expressoes);
                        }
                        
                        previewView.setValueSugested(columnValue);
                        previewView.setIsValidated(isValidated ? "Sim" : "Não");
                        
                        if (!isValidated) {
                            for (Acao acao: this.arquivo.getCampos().get(indexColumn).getAcoes()) {
                                List<Condicao> condicoesRegra = new ArrayList<>();
                                condicoesRegra.add(new Condicao(null, acao.getOperacao(), acao.getValor(), (short) 1));
                                List<String[]> expressoes = Utilidades.expressaoMatematica(condicoesRegra, previewView.getValueSugested()); // Molda em forma de expressão
                                expressoes.forEach(expressao -> {
                                    System.out.println("Expressão: " + Arrays.toString(expressao));
                                });
                                if (Validador.validacaoOu(campoAtual.getTipoDado(), expressoes)) {
                                    previewView.setValueSugested(acao.getNovoValor());
                                }
                            }
                        }
                        
                        previewView.setValueSugested(previewView.getValue().equals(previewView.getValueSugested()) ? previewView.getValue(): previewView.getValue() + " -> " + previewView.getValueSugested());

                        this.previewViews.add(previewView);
                    }
                }

                if (tpPreview.isCollapsible() && !tpPreview.isExpanded()) {
                    tpPreview.setExpanded(true);
                }
            } else {

            }
        } catch (IOException ex) {
            Utilidades.AlertErro("ERRO", 
                    "ARQUIVO DE PROPRIEDADES", 
                    "Arquivo de propriedades não encontrado no caminho correto.\n\nErro: " + ex.getMessage());
        }
    }
    
    @FXML
    void btnAddAction_OnAction(ActionEvent event) {
        Operacao operacao = cbCondicaoAction.getValue();
        String valor = txtValorAction.getText();
        String novoValor = txtNovoValorAction.getText();
        
        final int index = this.campoAtual.getAcoes().size();
        Acao acao = new Acao(operacao, valor, novoValor, (short) (index + 1), campoAtual);

        if (operacao == null) {
            Utilidades.AlertErro("ERRO", "OPERAÇÃO", "Por favor selecione a operação antes de adicionar a ação de correção.");
            
            cbCondicaoAction.requestFocus();
        } else if (this.campoAtual.getAcoes().contains(acao)) {
            Utilidades.AlertErro("ERRO", "AÇÃO JÁ EXISTE", "Ação já está cadastrada.");
        } else {
            addAcao(acao);
            
            cbCondicaoAction.setValue(null);
            txtValorAction.setText("");
            txtNovoValorAction.setText("");
            cbCondicaoAction.requestFocus();
        }
    }
    
    @FXML
    void btnRemoveAction_OnAction(ActionEvent event) {
        ObservableList<Acao> acoesRemover = tvActions.getSelectionModel().getSelectedItems();
        if (acoesRemover.isEmpty()) {
            Utilidades.AlertErro("ERRO", "SELECIONAR AÇÃO", "Selecione pelo menos uma ação antes de remover um dos itens.");
        } else {
            Alert alert;
            if (acoesRemover.size() > 1) {
                alert = Utilidades.AlertYesNo("AVISO", "REMOVER AÇÕES", "Deseja realmente remover as ações selecionadas?");
            } else {
                alert = Utilidades.AlertYesNo("AVISO", "REMOVER AÇÕES", "Deseja realmente remover a ação selecionada?");
            }
            alert.showAndWait().ifPresent(button -> {
                if (button == ButtonType.YES) {
                    removeAcao(acoesRemover);
                    
                    for (int i = 0; i < this.campoAtual.getAcoes().size(); i++) {
                        this.campoAtual.getAcoes().get(i).setOrdem((short) (i + 1));
                    }
                }
            });
        }
    }
    
    @FXML
    void txtNovoValor_OnKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnAddAction_OnAction(new ActionEvent(btnAddAction, btnAddAction));
        }
    }
    
    @FXML
    void btnValidar_OnAction(ActionEvent event) {
        try {
            gravarInformacoes();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/gerenciar/ValidarBaseDados.fxml"));
            Stage stage = GerenciarCamposControle.managerStage;
            loader.setController(new ValidarBaseDadosControle(arquivo, dados));
            
            stage.setScene(new Scene(loader.load()));
            stage.setMaximized(Utilidades.isMAXIMAZED());
            Utilidades.SetStageDates(stage, "Epi Data Cleaner - Validação dos campos");
            stage.show();
        } catch (IOException ex) {
            Utilidades.AlertErro("ERRO", 
                "ERRO INESPERADO", 
                "Erro ao abrir a janela de \"Gerenciamento de Campos\".\n\nErro:\n" + ex.getMessage() + "\nCausado Por:\n" + ex.getCause().getMessage());
        }
    }
    
    @FXML
    void cbCondicaoAction_OnAction(ActionEvent event) {
        ComboBox<Operacao> operacaoCombo = (ComboBox<Operacao>) event.getSource();
        if (operacaoCombo.getSelectionModel().getSelectedIndex() >= 0) {
            boolean habilitar = !operacaoCombo.getSelectionModel().getSelectedItem().isHabilitarValor();
            txtValorAction.setDisable(habilitar);
            //txtNovoValorAction.setDisable(habilitar);
        }
    }
    
    @FXML
    void miExportarTemplate_OnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/gerenciar/ExportarTemplate.fxml"));
            Stage stage = new Stage();
            loader.setController(new ExportarTemplateControle(stage, arquivo));
            
            stage.setScene(new Scene(loader.load()));
            stage.setMaximized(Utilidades.isMAXIMAZED());
            Utilidades.SetStageDates(stage, "Epi Data Cleaner - Exportação do Template");
            stage.show();
            GerenciarCamposControle.getStage().hide();
        } catch (IOException ex) {
            Utilidades.AlertErro("ERRO", 
                "ERRO INESPERADO", 
                "Erro ao abrir a janela de \"Exportação do Template\".\n\nErro:\n" + ex.getMessage() + "\nCausado Por:\n" + ex.getCause().getMessage());
        }
    }

    @FXML
    void miImportarTemplate_OnAction(ActionEvent event) {
        
    }
    
    @FXML
    void miSobre_OnAction(ActionEvent event) {
        
    }

    @FXML
    void miSair_OnAction(ActionEvent event) {
        
    }
    
    public static Stage getStage() {
        return GerenciarCamposControle.managerStage;
    }
    
    private void iniciarCampos() {
        arquivo.getCampos().forEach(campo -> {
            final String fieldName = campo.getNome();
            
            ToggleButton btnToggle = new ToggleButton(fieldName.toUpperCase());
            
            btnToggle.setMinSize(40, 25);
            btnToggle.setMaxSize(150, 25);
            btnToggle.autosize();
            btnToggle.setToggleGroup(tgCampos);
            
            btnToggle.setOnAction(event -> {
                ToggleButton toggleSelected  = (ToggleButton) event.getSource();
                boolean isSelected = toggleSelected.isSelected();
                
                tpManage.setText(isSelected ? "Regras de validação/Ações de correção - " + fieldName.toUpperCase() : "");
                tpPreview.setText(isSelected ? "Prévia - " + fieldName.toUpperCase() : "");
                lblAction.setText(fieldName.toUpperCase().trim()); // Modifica o nome na lblAction para o nome do Campo.
                btnPreview.setDisable(!isSelected);
                
                this.indiceCampoSelecionado = hBoxCampos.getChildren().indexOf(toggleSelected);
                
                if (isSelected) {
                    tpManage.setCollapsible(isSelected);
                    tpPreview.setCollapsible(isSelected);
                    
                    this.campoAtual = this.arquivo.getCampos().get(indiceCampoSelecionado);
                } else {
                    tpManage.setExpanded(isSelected);
                    tpManage.setCollapsible(isSelected);
                    tpPreview.setExpanded(isSelected);
                    tpPreview.setCollapsible(isSelected);
                    
                    this.campoAtual = null;
                }
                
                recarregarVisualizacaoCampo();
            });
            
            hBoxCampos.getChildren().add(btnToggle);
            
            // Força a seleção do primeiro Toggle na abertura da janela.
            if (arquivo.getCampos().get(0).equals(campo)) {
                btnToggle.setSelected(true);
                btnToggle.getOnAction().handle(new ActionEvent(btnToggle, btnToggle));
            }
        });
    }
    
    private void iniciarComboTipoDados() {
        tiposDados.clear();
        tiposDados.add("Selecione");
        for (TipoDadoEnum tipoDado: TipoDadoEnum.values()) {
            tiposDados.add(tipoDado.toString());
        }
        
        cbTipoDado.setItems(tiposDados);
        cbTipoDado.setValue("Selecione");
        
        cbTipoDado.setOnAction((event) -> {
            /*if (!this.campoAtual.getRegras().isEmpty()) {
                if (TipoDadoEnum.valueOf(cbTipoDado.getValue()) != campoAtual.getTipoDado()) {
                    Alert alert = Utilidades.AlertYesNo("AVISO", "REGRAS JÁ VINCULADAS", "Regras já estão vinculadas ao campo, deseja realmente mudar o tipo do dado? Caso continue essas regras dixarão de estar associadas ao campo.");
                    alert.showAndWait().ifPresent(button -> {
                        if (button == ButtonType.YES) {
                            alterarTipagemCampo(event);
                        } else {
                            cbTipoDado.getSelectionModel().select(campoAtual.getTipoDado().toString());
                        }
                    });
                } else {
                    alterarTipagemCampo(event);
                }
            } else {*/
                alterarTipagemCampo(event);
            //}
        });
    }
    
    private void alterarTipagemCampo(ActionEvent event) {
        if (cbTipoDado.getSelectionModel().getSelectedIndex() > 0) {
            if (campoAtual != null) {
                if (TipoDadoEnum.valueOf(cbTipoDado.getValue()) != campoAtual.getTipoDado()) {
                    Hibernate.initialize(campoAtual.getRegras());
                    campoAtual.getRegras().forEach(regra -> {
                        regra.getCampos().remove(campoAtual);
                    });
                    campoAtual.getRegras().clear();
                    
                    Hibernate.initialize(campoAtual.getAcoes());
                    campoAtual.getAcoes().forEach(acao -> {
                        acao.setCampo(null);
                    });
                    campoAtual.getAcoes().clear();
                }
                campoAtual.setTipoDado(TipoDadoEnum.valueOf(cbTipoDado.getValue()));
                atualizarListaRegras(event);
                atualizarListaAcoes(event);
            }
        } else {
            if (campoAtual != null) {
                campoAtual.setTipoDado(null);
                Hibernate.initialize(campoAtual.getRegras());
                campoAtual.getRegras().forEach(regra -> {
                    regra.getCampos().remove(campoAtual);
                });
                campoAtual.getRegras().clear();
                
                Hibernate.initialize(campoAtual.getAcoes());
                campoAtual.getAcoes().forEach(acao -> {
                    acao.setCampo(null);
                });
                this.campoAtual.getAcoes().clear();
                this.vBoxRegrasAtuais.getChildren().clear();
            }
        }
    }
    
    private void atualizarListaRegras (ActionEvent eventoCombo) {
        DaoRegra dao = new DaoRegra();
        // Busca regras do tipo de dado selecionado no combo respectivo ao campo.
        this.regrasAtuais = FXCollections.observableArrayList(dao.findRegrasByReference(TipoDadoEnum.valueOf(cbTipoDado.getValue())));
        
        this.vBoxRegrasAtuais.getChildren().clear();
        this.regrasAtuais.forEach(regra -> {
            CheckBox checkBox = new CheckBox(regra.toString());
            checkBox.setMinHeight(25);
            checkBox.setPrefHeight(25);
            checkBox.setMaxHeight(25);

            checkBox.setOnAction((ActionEvent event) -> {
                boolean isSelect = ((CheckBox) event.getSource()).isSelected();

                // Guarda a regra que foi selecionada no checkBox
                Regra regraSelecionada = this.regrasAtuais.get(vBoxRegrasAtuais.getChildren().indexOf(event.getSource()));

                List<Regra> regras = campoAtual.getRegras();
                // Armazena a regra caso esteja sendo selecionada e não estiver dentro da lista.
                if (isSelect && !regras.contains(regraSelecionada)) {
                    regraSelecionada.getCampos().add(campoAtual);
                    regras.add(regraSelecionada);
                // Remove a regra caso tenha sido desselecionada e esteja dentro da lista.
                } else if (!isSelect && regras.contains(regraSelecionada)) {
                    regraSelecionada.getCampos().remove(campoAtual);
                    regras.remove(regraSelecionada);
                }
            });

            vBoxRegrasAtuais.getChildren().add(checkBox);
        });
    }
    
    private void atualizarListaAcoes (ActionEvent eventoCombo) {
        if (cbTipoDado.getSelectionModel().getSelectedIndex() > 0) {
            tvActions.setItems(FXCollections.observableArrayList(this.campoAtual.getAcoes()));
            
            cbCondicaoAction.setItems(FXCollections.observableArrayList(new DaoOperacao().findOperacoesByTipoDado(this.arquivo.getCampos().get(indiceCampoSelecionado).getTipoDado())));
            cbCondicaoAction.setValue(null);
        } else {
            cbCondicaoAction.setItems(null);
            tvActions.setItems(null);
        }
    }
    
    private void iniciarEventos() {
        tpManage.expandedProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setTopAnchor(btnPreview, newValue ? 435.00 : 185.00);
            AnchorPane.setTopAnchor(tpPreview, newValue ? 470.00 : 220.00);
            
            mainAnchorPane.setPrefHeight(mainAnchorPane.getPrefHeight() + (newValue ? 250.00 : -250.00));
        });
        
        tpPreview.expandedProperty().addListener((observable, oldValue, newValue) -> {
            mainAnchorPane.setPrefHeight(mainAnchorPane.getPrefHeight() + (newValue ? 250.00 : -250.00));
        });
    }
    
    private void recarregarVisualizacaoCampo() {
        if (this.campoAtual != null) {
            if (this.campoAtual.getTipoDado() != null) {
                cbTipoDado.setValue(this.campoAtual.getTipoDado().toString());
                cbTipoDado.getOnAction().handle(new ActionEvent(cbTipoDado, cbTipoDado));
                
                this.campoAtual.getRegras().forEach(regra -> {
                    CheckBox checkBox = (CheckBox) vBoxRegrasAtuais.getChildren().get(regrasAtuais.indexOf(regra));
                    checkBox.setSelected(true);
                });
                
                /*
                this.campoAtual.getRegras().forEach(regraAtual -> {
                    for (int i = 0; i < regrasAtuais.size(); i++) {
                        if (regrasAtuais.get(i).getId().equals(regraAtual.getId())) {
                            CheckBox checkBox = (CheckBox) vBoxRegrasAtuais.getChildren().get(i);
                            checkBox.setSelected(true);
                        }
                    }
                });
                */
            } else {
                cbTipoDado.setValue(null);
            }
            
            tvActions.setItems(FXCollections.observableArrayList(this.campoAtual.getAcoes()));
        }
        
        this.previewViews.clear();
    }
    
    private void iniciarColunasTvPreview() {
        tvPreview.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("line"));
        tvPreview.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("isValidated"));
        tvPreview.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("value"));
        tvPreview.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("valueSugested"));
        
        tvPreview.setItems(previewViews);
        tvPreview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        ((TableColumn<VisaoPrevia, Long>) tvPreview.getColumns().get(0)).setCellFactory(column -> {
            return new TableCell<VisaoPrevia, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText("");
                    } else {
                        setText(String.valueOf(item));
                        
                        if (Utilidades.NumberOccurrences(getTableView().getItems().get(getIndex()).getValueSugested(), "->") != 1) {
                            setTextFill(Color.BLACK);
                        }
                    }
                }
            };
        });
        
        ((TableColumn<VisaoPrevia, String>) tvPreview.getColumns().get(1)).setCellFactory(column -> {
            return new TableCell<VisaoPrevia, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText("");
                    } else {
                        setText(String.valueOf(item));
                        
                        if (Utilidades.NumberOccurrences(getTableView().getItems().get(getIndex()).getValueSugested(), "->") != 1) {
                            setTextFill(Color.BLACK);
                        }
                    }
                }
            };
        });
        
        ((TableColumn<VisaoPrevia, String>) tvPreview.getColumns().get(2)).setCellFactory(column -> {
            return new TableCell<VisaoPrevia, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText("");
                    } else {
                        setText(String.valueOf(item));
                        
                        if (Utilidades.NumberOccurrences(getTableView().getItems().get(getIndex()).getValueSugested(), "->") != 1) {
                            setTextFill(Color.BLACK);
                        }
                    }
                }
            };
        });
        
        ((TableColumn<VisaoPrevia, String>) tvPreview.getColumns().get(3)).setCellFactory(column -> {
            return new TableCell<VisaoPrevia, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText("");
                        getTableRow().setStyle("");
                    } else {
                        setText(item);
                        
                        if (!getTableView().getItems().get(getIndex()).getValue().equals(item)) {
                            getTableRow().selectedProperty().addListener((observable, oldValue, newValue) -> {
                                getTableRow().setStyle(newValue ? "-fx-background-color: green": "-fx-background-color: lightgreen");
                            });
                            
                            getTableRow().setStyle("-fx-background-color: lightgreen");
                        } else {
                            getTableRow().selectedProperty().addListener((observable, oldValue, newValue) -> {
                                getTableRow().setStyle(newValue ? "-fx-background-color: gainsboro": "-fx-background-color: ghostwhite");
                            });
                            
                            setTextFill(Color.BLACK);
                            getTableRow().setStyle("-fx-background-color: ghostwhite");
                        }
                    }
                }
            };
        });
    }
    
    private void iniciarColunasTvActions() {
        tvActions.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("ordem"));
        tvActions.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("operacao"));
        tvActions.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("valor"));
        tvActions.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("novoValor"));
        
        tvActions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        tvActions.setItems(null);
    }
    
    private void addAcao(Acao acao) {
        this.campoAtual.getAcoes().add(acao);
        this.tvActions.getItems().add(acao);
    }
    
    private void removeAcao(List<Acao> acao) {
        this.campoAtual.getAcoes().removeAll(acao);
        this.tvActions.getItems().removeAll(acao);
    }
    
    private void gravarInformacoes() {
        DaoArquivo daoArquivo = new DaoArquivo();
        //Arquivo arquivoBanco = daoArquivo.findArquivoByPath(arquivo.getCaminho() + "\\" + arquivo.getNome());
        /*Hibernate.initialize(arquivo.getCampos());
        arquivo.getCampos().forEach(campo -> {
            Hibernate.initialize(campo.getRegras());
        });
        arquivo.getCampos().forEach(campo -> {
            Hibernate.initialize(campo.getAcoes());
        });*/
        
        daoArquivo.update(arquivo);
        
        /*DaoCampo daoCampo = new DaoCampo();
        arquivo.getCampos().forEach(campo -> {
            daoCampo.update(campo);
        });*/
        
    }
    
    private void carregarArquivo() {
        Hibernate.initialize(arquivo.getCampos());
        arquivo.getCampos().forEach(campo -> {
            Hibernate.initialize(campo.getRegras());
        });
        arquivo.getCampos().forEach(campo -> {
            Hibernate.initialize(campo.getAcoes());
        });
    }
    
}