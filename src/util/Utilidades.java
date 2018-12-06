package util;

import dao.DaoCondicao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modelo.Acao;
import modelo.Campo;
import modelo.Condicao;
import modelo.OperadorLogicoEnum;
import modelo.Regra;

public class Utilidades {
    
    private static final Image APP_ICON;
    private static boolean MAXIMAZED;
    public final static String CAMINHO_EXECUTAVEL;
    
    static {
        APP_ICON = new Image("/util/icons/main-icon.png");
        MAXIMAZED = false;
        CAMINHO_EXECUTAVEL = new File("").getAbsolutePath();
        System.out.println("Caminho executável: " + CAMINHO_EXECUTAVEL);
    }

    public static boolean isMAXIMAZED() {
        return MAXIMAZED;
    }

    public static void setIS_MAXIMAZED(boolean MAXIMAZED) {
        Utilidades.MAXIMAZED = MAXIMAZED;
    }
    
    public static Image getIconApplication() {
        return Utilidades.APP_ICON;
    }
    
    public static void SetStageDates(Stage stage, String title, boolean isResizable) {
        stage.setTitle(title);
        stage.setResizable(isResizable);
    }
    
    public static void SetStageDates(Stage stage, String title) {
        stage.setTitle(title);
        stage.setResizable(scenesAreResizable());
    }
    
    public static boolean scenesAreResizable(){
        return false;
    }
    
    public static boolean SameDelimiterCount(String[] linhas, String delimiter) {
        if (linhas.length > 0) {
            int numberDelimiters = NumberOccurrences(linhas[0], delimiter);
            
            for (int i = 0; i < linhas.length; i++) {
                if (numberDelimiters != NumberOccurrences(linhas[i], delimiter)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERRO");
                    alert.setHeaderText("INCONSISTÊNCIA NA QUANTIDADE DE DELIMITADORES");
                    alert.setContentText("Linha " + i + " não possui a mesma quantidade de delimitadores do cabeçalho.");
                    alert.showAndWait();
                    
                    return false;
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRO");
            alert.setHeaderText("NENHUM REGISTRO");
            alert.setContentText("Não há dados suficientes para análise.");
            alert.showAndWait();
            
            return false;
        }
        
        return true;
    }
    
    // Número de ocorrências de uma string dentro de outra.
    public static int NumberOccurrences(String linha, String delimiter) {
        return linha.replaceAll("[^" + delimiter + "]", "").length() / delimiter.length();
    }
    
    public static String[] SplitAlways(String linha, String delimiter) {
        String[] dados = linha.replaceAll(delimiter, " " + delimiter + " ").split(delimiter);
        for (int i = 0; i < dados.length; i++) {
            dados[i] = dados[i].trim();
        }
        return dados;
    }
    
    public static int SumColumnWidths(ObservableList<TableColumn<ObservableList<String>, ?>> columns) {
        int result = 0;
        for (TableColumn column: columns) {
            result += column.getWidth();
        }
        return result;
    }
    
    public static String concatConditions(List<Condicao> conditions) {
        String condicoes = "";
        
        for (Condicao condicao: conditions) {
            if (condicao.equals(conditions.get(0))) {
                condicoes += "\tcampo " + condicao.getOperacao().getNome() + " \"" + condicao.getValorEsperado() + "\"";
            } else {
                condicoes += " " + condicao.getOperadorLogico() + " campo " + condicao.getOperacao().getNome() + " \"" + condicao.getValorEsperado() + "\"";
            }
        }
        
        return condicoes;
    }
    
    // Monta as condições em forma de expressão matemática, com os parênteses.
    public static List<String[]> expressaoMatematica(List<Condicao> condicoes, String valorAtual) throws IOException, FileNotFoundException {
        String separador = ManipularProperties.getPropriedade().getProperty("separator.expression");
        
        String expressao = "";
        String valorAtualSeparado = separador + valorAtual + separador;
        
        for (Condicao condicao: condicoes) {
            String valorEsperadoSeparado = separador + condicao.getValorEsperado() + separador;
            
            if (condicao.getOrdem() == 1) {
                expressao = 
                        "(" + valorAtualSeparado + " " + 
                        condicao.getOperacao().getNome().replaceAll(" ", "") + 
                        " " + valorEsperadoSeparado;
            } else {
                if (condicao.getOperadorLogico() == OperadorLogicoEnum.E) {
                    expressao += 
                            " E " + valorAtualSeparado + " " + 
                            condicao.getOperacao().getNome().replaceAll(" ", "") + 
                            " " + valorEsperadoSeparado;
                } else {
                    expressao += 
                            ") OU (" + valorAtualSeparado + " " + 
                            condicao.getOperacao().getNome().replaceAll(" ", "") + 
                            " " + valorEsperadoSeparado;
                }
            }
        }
        
        expressao += ")";
        
        List<String> expressoes = new ArrayList<>();
        int qtdeParentese = NumberOccurrences(expressao, "(");
        int ultimaAbertura = -1, ultimaFechamento = -1;
        for (int i = 0; i < qtdeParentese; i++) {
            ultimaAbertura = expressao.indexOf("(", ultimaAbertura + 1);
            ultimaFechamento = expressao.indexOf(")", ultimaFechamento + 1);
            
            expressoes.add(expressao.substring(ultimaAbertura + 1, ultimaFechamento));
        }
        
        List<String[]> resultados = new ArrayList<>();
        
        expressoes.forEach(exp -> {
            resultados.add(exp.split(" E "));
        });
        
        return resultados;
    }
    
    public boolean validarRegras(Campo campo, String dado) throws IOException {
        
        for (Regra regra: campo.getRegras()) {
            List<Condicao> condicoesRegra = new DaoCondicao().findCondicoesByReference(regra); // Condições da regra
            
            List<String[]> expressoes = Utilidades.expressaoMatematica(condicoesRegra, dado); // Molda em forma de expressão
            expressoes.forEach(expressao -> {
                System.out.println("Expressão: " + Arrays.toString(expressao));
            });
            
            return Validador.validacaoOu(campo.getTipoDado(), expressoes);
        }
        
        return false;
    }
    
    public String aplicarAcao(Campo campo, String dado) throws IOException {
        for (Acao acao: campo.getAcoes()) {
            List<Condicao> condicoesRegra = new ArrayList<>();
            condicoesRegra.add(new Condicao(null, acao.getOperacao(), acao.getValor(), (short) 1));
            List<String[]> expressoes = Utilidades.expressaoMatematica(condicoesRegra, dado); // Molda em forma de expressão
            expressoes.forEach(expressao -> {
                System.out.println("Expressão: " + Arrays.toString(expressao));
            });
            if (Validador.validacaoOu(campo.getTipoDado(), expressoes)) {
                return acao.getNovoValor();
            }
        }
        
        return dado;
    }
    
    public static SpinnerValueFactory.DoubleSpinnerValueFactory Spinner(double min, double max, double value) {
        return new SpinnerValueFactory.DoubleSpinnerValueFactory (min, max, value) {
            @Override
            public void decrement(int steps) {
                this.setValue(this.getValue() - steps);
                if (this.getValue() < this.getMin()) {
                    this.setValue(this.getMin());
                }
            }

            @Override
            public void increment(int steps) {
                this.setValue(this.getValue() + steps);
                if (this.getValue() > this.getMax()) {
                    this.setValue(this.getMax());
                }
            }
        };
    }
    
    public static SpinnerValueFactory.IntegerSpinnerValueFactory Spinner(int min, int max, int value) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory (min, max, value) {
            @Override
            public void decrement(int steps) {
                this.setValue(this.getValue() - steps);
                if (this.getValue() < this.getMin()) {
                    this.setValue(this.getMin());
                }
            }

            @Override
            public void increment(int steps) {
                this.setValue(this.getValue() + steps);
                if (this.getValue() > this.getMax()) {
                    this.setValue(this.getMax());
                }
            }
        };
    }
    
    public static void AlertErro(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
    
    public static void AlertSucesso(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.getButtonTypes().remove(ButtonType.CANCEL);
        alert.showAndWait();
    }
    
    public static Alert AlertYesNo(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        return alert;
    }
    
    public static void AlertInformation(String title, String header, String context) {
        Alert removeAlert = new Alert(Alert.AlertType.INFORMATION);
        removeAlert.setTitle(title);
        removeAlert.setHeaderText(header);
        removeAlert.setContentText(context);
        removeAlert.showAndWait();
    }
    
    public static String[] retornaTresPartes(String expressao) throws IOException {
        String separador = ManipularProperties.getPropriedade().getProperty("separator.expression");
        
        if (!checkQtdeOcurrence(expressao, separador, 4)) return null;
        
        int[] ocorrencias = new int[4];
        ocorrencias[0] = expressao.indexOf(separador);
        for (int i = 1; i < 4; i++) {
            ocorrencias[i] = ocorrencias[i - 1] + expressao.substring(ocorrencias[i - 1] + separador.length()).indexOf(separador) + separador.length();
        }
        
        String[] resultado = new String[3];
        resultado[0] = expressao.substring(ocorrencias[0] + separador.length(), ocorrencias[1]).trim();
        resultado[1] = expressao.substring(ocorrencias[1] + separador.length(), ocorrencias[2]).trim();
        resultado[2] = expressao.substring(ocorrencias[2] + separador.length(), ocorrencias[3]).trim();
        
        return resultado;
    }
    
    public static boolean checkQtdeOcurrence(String linha, String ocorrencia, int qtdeOcorrencia) {
        return Utilidades.NumberOccurrences(linha, ocorrencia) == qtdeOcorrencia;
    }
    
    // Ajustando valor para ser aceita em forma de número fracionário.
    public static String transformaParaFlutuante(String valor) {
        if (valor.contains(",") && valor.contains(".")) valor = valor.replaceAll("\\.", "").replaceAll(",", ".");
        while (Utilidades.NumberOccurrences(valor, ",") > 1) {
            valor = valor.replaceFirst(",", "");
        }
        if (valor.contains(",")) valor = valor.replaceAll(",", ".");
        if (!valor.contains(".")) valor = valor + ".00";
        
        return valor;
    }
    
    public static String corpoMensagem(String variavel, Exception ex) {
        return variavel + "\n\nErro:\n" + ex.getMessage() + "\n\nCausado por:\n" + ex.getCause().getMessage();
    }
    
}