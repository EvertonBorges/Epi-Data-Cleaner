package controle.regra;

import dao.DaoRegra;
import controle.gerenciar.GerenciarCamposControle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.Regra;
import util.Utilidades;

public class PerquisarControle implements Initializable {
    
    private static Stage rulesStage;
    private ObservableList<Regra> filteredData;
    private ObservableList<Regra> regras;
    private ObservableList<String> filtros;
    private Regra regra;

    @FXML
    private TableView<Regra> tvDados;

    @FXML
    private TableColumn<Regra, String> tcTipo;

    @FXML
    private TableColumn<Regra, String> tcTitulo;

    @FXML
    private TableColumn<Regra, String> tcDescricao;

    @FXML
    private ComboBox<String> cbFiltro;

    @FXML
    private TextField txtFiltro;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initComboOperadoresLogicos();
        initTableView();
    }

    public PerquisarControle() {
        
    }

    public PerquisarControle(Stage rulesStage) {
        PerquisarControle.rulesStage = rulesStage;
    }

    @FXML
    void btnNovaRegra_OnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/regra/Novo.fxml"));
        Stage stage = PerquisarControle.rulesStage;
        loader.setController(new NovoControle());
        
        stage.setScene(new Scene(loader.load()));
        stage.setMaximized(Utilidades.isMAXIMAZED());
        Utilidades.SetStageDates(stage, "Epi Data Cleaner - Nova Regra de Validação");
        stage.show();
    }

    @FXML
    void btnVoltar_OnAction(ActionEvent event) {
        GerenciarCamposControle.getStage().show();
        getStage().close();
    }

    @FXML
    void tvDados_OnMouseReleased(MouseEvent event) throws IOException {
        if (event.getButton() == MouseButton.PRIMARY) {
            if (event.getClickCount() > 1) {
                regra = tvDados.getSelectionModel().getSelectedItem();
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizacao/regra/Detalhes.fxml"));
                loader.setController(new DetalhesControle(regra));
                
                Stage stage = PerquisarControle.rulesStage;
                
                stage.setMaximized(Utilidades.isMAXIMAZED());
                stage.setScene(new Scene(loader.load()));
                Utilidades.SetStageDates(stage, "Epi Data Cleaner - Detalhes Regra de Validação");
                stage.show();
            }
        }
    }
    
    public static Stage getStage() {
        return PerquisarControle.rulesStage;
    }
    
    private void initComboOperadoresLogicos() {
        filtros = FXCollections.observableArrayList();
        filtros.add("Selecione");
        filtros.add("Tipo de Dado");
        filtros.add("Título");
        filtros.add("Descrição");
        
        cbFiltro.setItems(filtros);
        cbFiltro.setValue("Selecione");
        
        cbFiltro.setOnAction((event) -> {
            if (cbFiltro.getSelectionModel().getSelectedIndex() > 0) {
                txtFiltro.setPromptText(filtros.get(cbFiltro.getSelectionModel().getSelectedIndex()) + "...");
            } else {
                txtFiltro.setPromptText("Pesquisar...");
            }
            updateFilteredData();
        });
    }
    
    private void initTableView() {
        tvDados.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tvDados.getItems().clear();
        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDado"));
        tcTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tcDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        filteredData = FXCollections.observableArrayList();
        
        txtFiltro.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            updateFilteredData();
        });
        
        loadTableView();
    }
    
    private void loadTableView() {
        DaoRegra dao = new DaoRegra();
        
        this.regras = FXCollections.observableArrayList(dao.findRegras("titulo", ""));
        
        tvDados.setItems(filteredData);
        
        updateFilteredData();
    }
    
    private void updateFilteredData() {
        filteredData.clear();
        
        regras.forEach(regraFiltro -> {
            if (matchesFilter(regraFiltro)) {
                filteredData.add(regraFiltro);
            }
        });
    }
    
    private boolean matchesFilter(Regra regra) {
        String filterString = txtFiltro.getText();
        
        if (filterString == null || filterString.isEmpty()) {
            return true;
        }
        
        String lowerCaseFilterString = filterString.toLowerCase();
        
        int filtroSelecionado = cbFiltro.getSelectionModel().getSelectedIndex();
        
        if (filtroSelecionado > 0) {
            switch (filtroSelecionado) {
                case 1:
                    if (regra.getTipoDado().toString().toLowerCase().contains(lowerCaseFilterString)) {
                        return true;
                    }   break;
                case 2:
                    if (regra.getTitulo().toLowerCase().contains(lowerCaseFilterString)) {
                        return true;
                    }   break;
                case 3:
                    if (regra.getDescricao().toLowerCase().contains(lowerCaseFilterString)) {
                        return true;
                    }   break;
                default:
                    break;
            }
        } else {
            if (regra.getTipoDado().toString().toLowerCase().contains(lowerCaseFilterString)) {
                return true;
            }
            if (regra.getTitulo().toLowerCase().contains(lowerCaseFilterString)) {
                return true;
            }
            if (regra.getDescricao().toLowerCase().contains(lowerCaseFilterString)) {
                return true;
            }
        }
        
        return false; // Does not match
    }
    
}
