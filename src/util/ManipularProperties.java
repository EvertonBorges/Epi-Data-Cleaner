package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ManipularProperties {
    
    private static final Properties PROPS = new Properties();
    private static final String MSG_TITULO = "AVISO";
    private static final String MSG_CABECALHO = "CONFIGURAÇÕES";
    private static final String MSG_CORPO = "Verifique se o arquivo de configurações está na pasta correta.";

    public static Properties getPropriedade() throws FileNotFoundException, IOException {
        FileInputStream file = new FileInputStream(Utilidades.CAMINHO_EXECUTAVEL + "/app.properties");
        PROPS.load(file);
        return PROPS;
    }
    
    public static void setPropriedade(String key, String valor) throws FileNotFoundException, IOException {
        FileOutputStream fileOutput = new FileOutputStream(Utilidades.CAMINHO_EXECUTAVEL + "/app.properties");
        FileInputStream fileInput = new FileInputStream(Utilidades.CAMINHO_EXECUTAVEL + "/app.properties");
        PROPS.load(fileInput);
        PROPS.setProperty(key, valor);
        PROPS.store(fileOutput, "");
    }
    
    public static void AlertaProperty(IOException ex) {
        Utilidades.AlertInformation(MSG_TITULO, MSG_CABECALHO, MSG_CORPO + "\n\nErro:\n" + ex.getMessage() + "\n\nCausado por:\n" + ex.getCause().getMessage() );
    }
    
}
