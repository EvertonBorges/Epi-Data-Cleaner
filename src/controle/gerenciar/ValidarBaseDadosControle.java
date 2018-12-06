package controle.gerenciar;

import dao.DaoCondicao;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modelo.Acao;
import modelo.Arquivo;
import modelo.Campo;
import modelo.Condicao;
import modelo.Regra;
import util.Utilidades;
import util.Validador;

public class ValidarBaseDadosControle implements Initializable {
    
    private final Arquivo arquivo;
    private final String[][] dados;
    private final String[][] dadosCorrigidos;
    private final int qtdeColunas;
    
    private final String[][] dadosSugeridos;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        iniciarTableViewDados();
    }

    public ValidarBaseDadosControle(Arquivo arquivo, String[][] dados) {
        this.arquivo = arquivo;
        this.dados = dados;
        this.qtdeColunas = dados[0].length;
        this.dadosCorrigidos = new String[this.dados.length][this.qtdeColunas];
        for (int i = 0; i < dados.length; i++)
            System.arraycopy(dados[i], 0, this.dadosCorrigidos[i], 0, this.qtdeColunas);
        
        this.dadosSugeridos = new String[dados.length - 1][dados[0].length];
    }
    
    @FXML
    private TableView<ObservableList<String>> tvDados;

    @FXML
    void btnAplicar_OnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/gerenciar/DadosLimpos.fxml"));
            Stage stage = GerenciarCamposControle.getStage();
            loader.setController(new DadosLimposControle(arquivo, dados, dadosCorrigidos));
            
            stage.setScene(new Scene(loader.load()));
            stage.setMaximized(Utilidades.isMAXIMAZED());
            Utilidades.SetStageDates(stage, "Epi Data Cleaner - Dados limpos");
            stage.show();
        } catch (IOException ex) {
            Utilidades.AlertErro("ERRO", 
                "ERRO INESPERADO", 
                "Erro ao abrir a janela de \"Gerenciamento de Campos\".\n\nErro:\n" + ex.getMessage() + "\nCausado Por:\n" + ex.getCause().getMessage());
        }
    }

    @FXML
    void btnVoltar_OnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/gerenciar/GerenciarCampos.fxml"));
            Stage stage = GerenciarCamposControle.getStage();
            loader.setController(new GerenciarCamposControle(dados, arquivo));
            
            stage.setScene(new Scene(loader.load()));
            stage.setMaximized(Utilidades.isMAXIMAZED());
            Utilidades.SetStageDates(stage, "Epi Data Cleaner - Gerenciamento de campos");
            stage.show();
        } catch (IOException ex) {
            Utilidades.AlertErro("ERRO", 
                "ERRO INESPERADO", 
                "Erro ao abrir a janela de \"Gerenciamento de Campos\".\n\nErro:\n" + ex.getMessage() + "\nCausado Por:\n" + ex.getCause().getMessage());
        }
    }
    
    private void iniciarTableViewDados() {
        // Adicionar colunas à TableViewDados
        arquivo.getCampos().forEach(campo -> {
            int finalIdx = arquivo.getCampos().indexOf(campo);
            TableColumn<ObservableList<String>, String> coluna = new TableColumn<>(campo.getNome());
            
            coluna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
            coluna.setMinWidth(60);
            coluna.setMaxWidth(200);
            coluna.setEditable(false);
            coluna.setSortable(true);
            
            tvDados.getColumns().add(coluna);
            
            /*
            coluna.setCellFactory(column -> {
                return new TableCell<ObservableList<String>, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText("");
                            getTableRow().setStyle("");
                        } else {
                            setText(item);

                            if (!getTableView().getItems().get(getIndex()).equals(item)) {
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
            */
        });
        
        // Realiza checagem das regras de validações e aplica as ações de correção quando necessário.
        for (int i = 0; i < this.dadosCorrigidos.length; i++) { // Passando pelas linhas
            for (int j = 0; j < qtdeColunas; j++) { // Passando pelas colunas
                Campo campo = this.arquivo.getCampos().get(j); // Referência que contém as regras e ações configuradas para cada coluna.
                
                if (i != arquivo.getCabecalho()) {
                    String dado = this.dadosCorrigidos[i][j];
                    
                    boolean isValidated = false;
                    
                    try {
                        for (Regra regra: campo.getRegras()) {
                            List<Condicao> condicoesRegra = new DaoCondicao().findCondicoesByReference(regra); // Condições da regra

                            List<String[]> expressoes = Utilidades.expressaoMatematica(condicoesRegra, dado); // Molda em forma de expressão
                            /*expressoes.forEach(expressao -> {
                                System.out.println("Expressão: " + Arrays.toString(expressao));
                            });*/
                            isValidated = Validador.validacaoOu(campo.getTipoDado(), expressoes);
                        }

                        if (!isValidated) {
                            for (Acao acao: campo.getAcoes()) {
                                List<Condicao> condicoesRegra = new ArrayList<>();
                                condicoesRegra.add(new Condicao(null, acao.getOperacao(), acao.getValor(), (short) 1));
                                List<String[]> expressoes = Utilidades.expressaoMatematica(condicoesRegra, dado); // Molda em forma de expressão
                                /*expressoes.forEach(expressao -> {
                                    System.out.println("Expressão: " + Arrays.toString(expressao));
                                });*/
                                if (Validador.validacaoOu(campo.getTipoDado(), expressoes)) {
                                    dado = acao.getNovoValor();
                                }
                            }
                        }
                    } catch (IOException ex) {
                        Utilidades.AlertErro("ERRO", 
                                          "ARQUIVO DE PROPRIEDADES", 
                                          "Arquivo de propriedades não encontrado no caminho correto.\n\nErro: " + ex.getMessage());
                    }
                    
                    
                    dadosCorrigidos[i][j] = dado;
                }
            }
        }
        
        for (int i = 0; i < this.dadosCorrigidos.length; i++) { // Passando pelas linhas
            for (int j = 0; j < qtdeColunas; j++) { // Passando pelas colunas
                if (i < arquivo.getCabecalho()){ 
                    if (!dados[i][j].equals(dadosCorrigidos[i][j])) {
                        dadosSugeridos[i][j] = dados[i][j] + " -> " + dadosCorrigidos[i][j];
                    } else {
                        dadosSugeridos[i][j] = dados[i][j];
                    }
                } else if (i > arquivo.getCabecalho()) {
                    if (!dados[i][j].equals(dadosCorrigidos[i][j])) {
                        dadosSugeridos[i - 1][j] = dados[i][j] + " -> " + dadosCorrigidos[i][j];
                    } else {
                        dadosSugeridos[i - 1][j] = dados[i][j];
                    }
                }
            }
        }
        
        tvDados.getItems().clear();
        for (String[] dado: dadosSugeridos) {
            tvDados.getItems().add(FXCollections.observableArrayList(dado));
        }
    }
    
    
    
}
