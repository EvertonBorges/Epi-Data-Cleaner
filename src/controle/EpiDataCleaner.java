package controle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Utilidades;

public class EpiDataCleaner extends Application {
    
    private static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        EpiDataCleaner.stage = stage;
        
        Parent root = FXMLLoader.load(getClass().getResource("/visualizacao/SelecionarDados.fxml"));
        
        Scene scene = new Scene(root);
        
        Utilidades.SetStageDates(stage, "Epi Data Cleaner - Selecionar Tabela");
        stage.getIcons().add(Utilidades.getIconApplication());
        
        stage.setScene(scene);
        stage.setMaximized(Utilidades.isMAXIMAZED());
        
        stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            Utilidades.setIS_MAXIMAZED(newValue);
        });
        
        EpiDataCleaner.stage = stage;
        EpiDataCleaner.stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
