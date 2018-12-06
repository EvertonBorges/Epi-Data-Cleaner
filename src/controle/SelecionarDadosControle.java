package controle;

import controle.gerenciar.GerenciarCamposControle;
import dao.DaoArquivo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.persistence.NoResultException;
import modelo.Arquivo;
import modelo.Campo;
import modelo.Condicao;
import org.hibernate.Hibernate;
import util.JpaUtil;
import util.Utilidades;
import util.ManipularProperties;

public class SelecionarDadosControle implements Initializable {
    
    private final String[] formatosReconhecido = {".CSV"};
    private Arquivo arquivo;
    private String[][] dados;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        iniciarSpinner();
        iniciarTableView();
        btnTableSelect.setGraphic(new ImageView(new Image("/util/icons/folder16x16_1.png")));
        initTestes();
    }

    @FXML
    private TextField txtPathTableSelected;

    @FXML
    private TableView<ObservableList<String>> tvDatasSelecteds;
    
    @FXML
    private TextField txtDemiliter;

    @FXML
    private Spinner<Integer> spnHeadLine;
    
    @FXML
    private Button btnTableSelect;

    @FXML
    void btnExit_OnAction(ActionEvent event) {
        if (JpaUtil.existEntityManagerFactory()){
            JpaUtil.closeEntityManagerFactory();
        }
        
        Stage stage = EpiDataCleaner.getStage();
        
        stage.close(); // Fecha o stage.
        Platform.exit(); // Fecha a aplicação.
        System.exit(0); // Finaliza a execução.
    }

    @FXML
    void btnManageFields_OnAction(ActionEvent event) {
        if (dados != null && arquivo != null) {
            if ((dados[getLinhaCabecalho()].length != arquivo.getCampos().size()) || arquivo.getCampos().isEmpty()) {
                Utilidades.AlertErro("ERRO", "ERRO DADOS", "Carrege os dados antes de ir para o gerenciamento dos campos.");
                return;
            }
            
            for (int i = 0; i < dados[getLinhaCabecalho()].length; i++) {
                if (!dados[getLinhaCabecalho()][i].equals(arquivo.getCampos().get(i).getNome())) {
                    Utilidades.AlertErro("ERRO", "ERRO DADOS", "Carrege os dados antes de ir para o gerenciamento dos campos.");
                    return;
                }
            }
            
            try {
                String path = arquivo.getCaminho() + "\\" + arquivo.getNome();
                DaoArquivo dao = new DaoArquivo();
                
                try {
                    Arquivo arquivoBanco = dao.findArquivoByPath(path);
                    
                    /*if (arquivoBanco.getCampos().size() != arquivo.getCampos().size() ||
                        !arquivoBanco.getCampos().containsAll(arquivo.getCampos())) {
                        Alert alerta =
                                Utilidades.AlertYesNo("CONFLITO", "CONFLITO DE CAMPOS", 
                                        "Arquivo com campos diferentes do original. "
                                      + "Deseja continuar assim mesmo?"
                                      + "(Dados anteriores serão perdidos!!!)");
                        alerta.showAndWait().ifPresent(button -> {
                            if (button == ButtonType.YES) {
                                dao.update(arquivoBanco, arquivo.getCampos());
                                arquivo = arquivoBanco;
                            }
                        });
                    } else {*/
                        arquivo = arquivoBanco;
                    /*}*/
                } catch (NoResultException ex) {
                    dao.add(arquivo);
                }
                
                Stage stage = new Stage();
                Utilidades.SetStageDates(stage, "Epi Data Cleaner - Gerenciamento de campos");
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/gerenciar/GerenciarCampos.fxml"));
                loader.setController(new GerenciarCamposControle(stage, dados, arquivo));
                
                stage.setScene(new Scene(loader.load()));
                stage.getIcons().add(Utilidades.getIconApplication());
                stage.show();
                
                EpiDataCleaner.getStage().hide();
            } catch (Exception ex) {
                Utilidades.AlertErro("ERRO", 
                                  "ERRO INESPERADO", 
                                  "Erro ao abrir a janela de \"Gerenciamento de Campos\".\n\nErro:\n" + ex.getMessage() + "\nCausado por:\n" + ex.getCause().getMessage());
            }
        } else {
            Utilidades.AlertErro("ERRO", "ERRO DADOS", "Faça a leitura do arquivo e carregamento dos dados antes de continuar para o gerenciamento dos campos.");
        }
    }

    @FXML
    void btnTableSelect_OnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        try {
            String lastPath = ManipularProperties.getPropriedade().getProperty("file.lastDataPath");
            
            if (!lastPath.isEmpty()) {
                File diretorioPadrao = new File(lastPath);
                if (diretorioPadrao.exists()) {
                    fileChooser.setInitialDirectory(diretorioPadrao);
                }
            }
        } catch(IOException ex) {
            ManipularProperties.AlertaProperty(ex);
        }
        
        fileChooser.setTitle("Selecione a tabela");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            txtPathTableSelected.setText(file.getAbsolutePath());
            txtPathTableSelected_OnAction(null);
            String caminho = txtPathTableSelected.getText().substring(0, file.getAbsolutePath().lastIndexOf("\\"));
            
            try {
                ManipularProperties.setPropriedade("file.lastDataPath", caminho);
            } catch (IOException ex) {
                ManipularProperties.AlertaProperty(ex);
            }
        } else {
            txtPathTableSelected.setText("");
            txtPathTableSelected_OnAction(null);
        }
    }
    
    @FXML
    void txtPathTableSelected_OnAction(ActionEvent event) {
        boolean formatoValido = false;
        String file = txtPathTableSelected.getText();
        
        if (!file.isEmpty()) {
            String caminho = txtPathTableSelected.getText().substring(0, txtPathTableSelected.getText().lastIndexOf("\\"));
            String nomeArquivo = txtPathTableSelected.getText().substring(txtPathTableSelected.getText().lastIndexOf("\\") + 1);
            
            for (String formato: formatosReconhecido) {
                if (nomeArquivo.toUpperCase().endsWith(formato)) {
                    arquivo = new Arquivo();
                    arquivo.setCaminho(caminho);
                    arquivo.setNome(nomeArquivo);
                    txtPathTableSelected.setText(arquivo.getCaminho() + "\\" + arquivo.getNome());
                    //txtPathTableSelected.setText(file.getAbsolutePath());
                    CarregarArquivo();
                    formatoValido = true;
                    break;
                }
            }
            if (!formatoValido) {
                Utilidades.AlertErro("ERRO", "FORMATO NÃO ACEITO", "Arquivo \"" + nomeArquivo + "\" não corresponde a um tipo de arquivo aceito pela aplicação.");
            }
        } else {
            Utilidades.AlertErro("ERRO", "ARQUIVO NÃO SELECIONADO", "Nenhum arquivo foi selecionado");
        }
    }
    
    @FXML
    void btnLoadTable_OnAction(ActionEvent event) {
        if (dados != null) {
            if ((arquivo.getCaminho() + "\\" + arquivo.getNome()).equals(txtPathTableSelected.getText())) {
                CarregarDados();
            } else {
                Utilidades.AlertErro("ERRO", "FAÇA A LEITURA DOS DADOS", "Por favor faça novamente a leitura dos dados do arquivo.");
            }
        } else {
            Utilidades.AlertErro("ERRO", "DADOS NÃO SELECIONADOS", "Por favor informe antes os dados a serem lidos.");
        }
    }
    
    private void CarregarArquivo() {
        try {
            // Primeira leitura do arquivo para saber quantas linhas ele possui.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo.getCaminho() + "\\" + arquivo.getNome()), "UTF-8"));
            int numLinhas = 0;
            while ((bufferedReader.readLine() != null)) {
                numLinhas++;
            }
            bufferedReader.close();
            
            String[] linhasArquivo = new String[numLinhas];
            String linha;
            numLinhas = 0;
            
            // Segunda leitura para armazenar todas as linhas em "linhasArquivo".
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo.getCaminho() + "\\" + arquivo.getNome()), "UTF-8"));
            while ((linha = bufferedReader.readLine()) != null) {
                linhasArquivo[numLinhas] = linha;
                numLinhas++;
            }
            
            bufferedReader.close();
            
            spnHeadLine.setValueFactory(IntegerSpinner(0, linhasArquivo.length - 1, 0));
            spnHeadLine.setDisable(true);
            
            String delimitador = txtDemiliter.getText();
            try {
                if (delimitador.isEmpty()) {
                    txtDemiliter.setText(ManipularProperties.getPropriedade().getProperty("separator.delimiter"));
                    delimitador = txtDemiliter.getText();
                }
            } catch (IOException ex) {
                Utilidades.AlertErro("ERRO", 
                    "ARQUIVO DE PROPRIEDADES", 
                    "Arquivo de propriedades não encontrado no caminho correto.\n\nErro: " + ex.getMessage());
            }
            
            if (delimitador.isEmpty()) {
                txtDemiliter.setText(";");
                delimitador = txtDemiliter.getText();
            }
            
            if (Utilidades.SameDelimiterCount(linhasArquivo, delimitador)) {
                arquivo.setDelimitador(delimitador);
                arquivo.setCabecalho(0);

                int qtdeLinhas = linhasArquivo.length;
                int qtdeColunas = Utilidades.NumberOccurrences(linhasArquivo[0], delimitador) + 1;
                dados = new String[qtdeLinhas][qtdeColunas];

                for (int i = 0; i < qtdeLinhas; i++) {
                    dados[i] = Utilidades.SplitAlways(linhasArquivo[i], delimitador);
                }

                //LoadTable();

                spnHeadLine.setDisable(false);
            }
        } catch (FileNotFoundException ex) {
            Utilidades.AlertErro("ERRO", 
                    "ARQUIVO NÃO ENCONTRADO", 
                    "Arquivo não foi encontrado.\n\nErro: " + ex.getMessage());
        } catch (IOException ex) {
            Utilidades.AlertErro("ERRO", 
                    "ARQUIVO ABERTO", 
                    "Por favor feche qualquer janela do arquivo selecionado.\n\nErro: " + ex.getMessage());
        }
    }
    
    private void CarregarDados() {
        tvDatasSelecteds.getColumns().clear();
        arquivo.setCabecalho(getLinhaCabecalho());
        String[] cabecalho = dados[arquivo.getCabecalho()];
        
        tvDatasSelecteds.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        arquivo.getCampos().clear();
        
        for (int i = 0; i < cabecalho.length; i++) {
            final int finalIdx = i;
            Campo campo = new Campo();
            campo.setNome(cabecalho[i]);
            campo.setArquivo(arquivo);
            TableColumn<ObservableList<String>, String> coluna = new TableColumn<>(campo.getNome());
            coluna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
            tvDatasSelecteds.getColumns().add(coluna);
            arquivo.getCampos().add(campo);
        }
        
        List<String[]> corpo = new ArrayList<>();
        for (String[] linha: dados) {
            if (linha != cabecalho) {
                corpo.add(linha);
            }
        }
        
        tvDatasSelecteds.getItems().clear();
        corpo.forEach((dado) -> {
            tvDatasSelecteds.getItems().add(FXCollections.observableArrayList(dado));
        });
        
        tvDatasSelecteds.autosize();
        
        if (Utilidades.SumColumnWidths(tvDatasSelecteds.getColumns()) < 620) {
            tvDatasSelecteds.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tvDatasSelecteds.autosize();
        }
        
    }
    
    private void iniciarSpinner() {
        spnHeadLine.setValueFactory(IntegerSpinner(0, Integer.MAX_VALUE, 0));
        spnHeadLine.setDisable(true);
    }
    
    private void iniciarTableView() {
        tvDatasSelecteds.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tvDatasSelecteds.getColumns().clear();
    }
    
    private int getLinhaCabecalho() {
        if (spnHeadLine.getValue() < ((SpinnerValueFactory.IntegerSpinnerValueFactory) spnHeadLine.getValueFactory()).getMin() ||
            spnHeadLine.getValue() > ((SpinnerValueFactory.IntegerSpinnerValueFactory) spnHeadLine.getValueFactory()).getMax())
            return 0;
        else
            return spnHeadLine.getValue();
    }
    
    private SpinnerValueFactory.IntegerSpinnerValueFactory IntegerSpinner(int min, int max, int value) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory (min, max, value) {
            @Override
            public void decrement(int steps) {
                this.setValue(this.getValue() - steps);
                if (this.getValue() < this.getMin()) {
                    this.setValue(this.getMin());
                }

                CarregarDados();
            }

            @Override
            public void increment(int steps) {
                this.setValue(this.getValue() + steps);
                if (this.getValue() > this.getMax()) {
                    this.setValue(this.getMax());
                }

                CarregarDados();
            }
        };
    }
    
    private void initTestes() {
        List<Condicao> condicoes = new ArrayList<>();
        /*
        condicoes.add(new Condicao(null, new Operacao(TipoDadoEnum.TEXTUAL, "Igual a"), "M", (short) 1, null));
        condicoes.add(new Condicao(OperadorLogicoEnum.E, new Operacao(TipoDadoEnum.TEXTUAL, "Começado com"), "Mascu", (short) 2, null));
        condicoes.add(new Condicao(OperadorLogicoEnum.OU, new Operacao(TipoDadoEnum.TEXTUAL, "Terminado com"), "o", (short) 3, null));
        List<String[]> expressoes = Library.mathExpression(condicoes, "Masc ulin o");
        expressoes.forEach(expressao -> {
            System.out.println("Expressões: " + Arrays.toString(expressao));
        });
        System.out.println("Regra validada? " + Validator.ValidadorOr(TipoDadoEnum.TEXTUAL, expressoes));
        */
        
        /*
        condicoes.add(new Condicao(null, new Operacao(TipoDadoEnum.INTEIRO, "Menor que"), "5", (short) 1, null));
        condicoes.add(new Condicao(OperadorLogicoEnum.E, new Operacao(TipoDadoEnum.INTEIRO, "Maior que"), "100", (short) 2, null));
        condicoes.add(new Condicao(OperadorLogicoEnum.OU, new Operacao(TipoDadoEnum.INTEIRO, "Igual a"), "30", (short) 3, null));
        List<String[]> expressoes = Library.mathExpression(condicoes, "30");
        System.out.println("Regra validada? " + Validator.ValidadorOr(TipoDadoEnum.INTEIRO, expressoes));
        */
        
        /*
        condicoes.add(new Condicao(null, new Operacao(TipoDadoEnum.FRACIONARIO, "Menor que"), "5.00", (short) 1, null));
        condicoes.add(new Condicao(OperadorLogicoEnum.E, new Operacao(TipoDadoEnum.FRACIONARIO, "Maior que"), "100.00", (short) 2, null));
        condicoes.add(new Condicao(OperadorLogicoEnum.OU, new Operacao(TipoDadoEnum.FRACIONARIO, "Igual a"), "30.00", (short) 3, null));
        List<String[]> expressoes = Library.mathExpression(condicoes, "30,1");
        System.out.println("Regra validada? " + Validator.ValidadorOr(TipoDadoEnum.FRACIONARIO, expressoes));
        +*/
    }
    
}