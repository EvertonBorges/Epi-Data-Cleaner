package controle.gerenciar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.Arquivo;
import util.ManipularProperties;
import util.Utilidades;

public class DadosLimposControle implements Initializable {
    
    private final Arquivo arquivo;
    private final String[][] dados;
    private final String[][] dadosLimpos;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        carregarDados();
    }

    public DadosLimposControle(Arquivo arquivo, String[][] dados, String[][] dadosLimpos) {
        this.arquivo = arquivo;
        this.dados = dados;
        this.dadosLimpos = dadosLimpos;
    }
    
    @FXML
    private TableView<ObservableList<String>> tvDados;

    @FXML
    void btnExportarDados_OnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        
        try {
            String lastPath = ManipularProperties.getPropriedade().getProperty("file.lastExportPath");
            
            if (!lastPath.isEmpty()) {
                File diretorioPadrao = new File(lastPath);
                if (diretorioPadrao.exists()) {
                    fileChooser.setInitialDirectory(diretorioPadrao);
                }
            }
        } catch (IOException ex) {
            ManipularProperties.AlertaProperty(ex);
        }
            
        fileChooser.setTitle("Exportar dados");
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV (separado por vírgulas)", Arrays.asList("*.csv", "*.CSV")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV (separado por ponto e vírgula)", Arrays.asList("*.csv", "*.CSV")));
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pasta de Trabalho do Excel", Arrays.asList("*.xlsx", "*.XLSX")));
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pasta de Trabalho do Excel 97-2003", Arrays.asList("*.xls", "*.XLS")));
        File file = fileChooser.showSaveDialog(GerenciarCamposControle.getStage());
        if (fileChooser.getSelectedExtensionFilter() != null && fileChooser.getSelectedExtensionFilter().getExtensions() != null) {
            switch (fileChooser.getExtensionFilters().indexOf(fileChooser.getSelectedExtensionFilter())) {
                case 0:
                    /*exportToCsv(file, ",", dadosLimpos);
                    break;
                case 1:*/
                    exportToCsv(file, ";", dadosLimpos);
                    break;
                /*case 2:
                    Utilidades.AlertInformation("ERRO", "EM TESTE", "Funcionalidade ainda não implementada.");
                    break;
                case 3:
                    Utilidades.AlertInformation("ERRO", "EM TESTE", "Funcionalidade ainda não implementada.");
                    break;*/
            }
        }
    }
    
    @FXML
    void btnVoltar_OnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/gerenciar/ValidarBaseDados.fxml"));
            Stage stage = GerenciarCamposControle.getStage();
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
    
    private void carregarDados() {
        for (int i = 0; i < dadosLimpos[0].length; i++) {
            final int finalIdx = i;
            
            TableColumn<ObservableList<String>, String> coluna = new TableColumn<>(arquivo.getCampos().get(i).getNome());
            coluna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
            tvDados.getColumns().add(coluna);
        }
        
        for (String[] linha: dadosLimpos) {
            if (linha != dadosLimpos[arquivo.getCabecalho()]) {
                tvDados.getItems().add(FXCollections.observableArrayList(linha));
            }
        }
    }
    
    private void exportToCsv(File arquivo, String delimitador, String[][] dados) {
        try (FileWriter fileWriter = new FileWriter(arquivo, false)) {
            //BufferedWriter out = new BufferedWriter(fileWriter);
            
            OutputStreamWriter out = new OutputStreamWriter(
                                                    new FileOutputStream(arquivo),
                                                    Charset.forName("UTF-8").newEncoder() 
                                                );
            
            for (int i = 0; i < dados.length; i++) {
                for (int j = 0; j < dados[i].length; j++) {
                    if (j < dados[i].length - 1) {
                        out.write(dados[i][j] + delimitador);
                    } else {
                        out.write(dados[i][j]);
                        if (i < dados.length - 1) {
                            out.write("\n");
                        }
                    }
                }
            }

            //fileWriter.getEncoding();
            out.flush();
            out.close();
            
            fileWriter.close();
            
            String caminho = arquivo.getAbsolutePath().substring(0, arquivo.getAbsolutePath().lastIndexOf("\\"));
            ManipularProperties.setPropriedade("file.lastExportPath", caminho);
            
            Utilidades.AlertSucesso("ARQUIVO", "ARQUIVO EXPORTADO", "Arquivo '" + arquivo.getName() + "' exportado com SUCESSO.");
        } catch (IOException ex) {
            Utilidades.AlertErro("ERRO", "EXPORTAÇÃO DE DADOS", "Erro inesperado ao realizar a exportação dos dados.\n\nErro:\n" + ex.getMessage());
        }
        
    }
    
}
