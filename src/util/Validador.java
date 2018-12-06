package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import modelo.TipoDadoEnum;

public class Validador {
    
    private static boolean validadorRegex(TipoDadoEnum tipoDadoEnum, String valor) throws IOException, FileNotFoundException {
        Properties properties = ManipularProperties.getPropriedade();
        
        switch (tipoDadoEnum) {
            case TEXTUAL: return valor.matches(properties.getProperty("regex.TEXT"));
            case INTEIRO: return valor.matches(properties.getProperty("regex.INTEGER"));
            case FRACIONARIO: return valor.matches(properties.getProperty("regex.FLOAT"));
            case DATA: return valor.matches(properties.getProperty("regex.DATE"));
        }
        return false;
    }
    
    public static boolean validacaoOu(TipoDadoEnum tipoDadoEnum, List<String[]> expressoes) throws IOException, FileNotFoundException {
        for (String[] expressao: expressoes) {
            if (validacaoE(tipoDadoEnum, expressao)) return true;
        }
        
        return false;
    }
    
    public static boolean validacaoOu(TipoDadoEnum tipoDadoEnum, String[] expressao) throws IOException, FileNotFoundException {
        return (validacaoE(tipoDadoEnum, expressao)) ;
    }
    
    private static boolean validacaoE(TipoDadoEnum tipoDadoEnum, String[] expressao) throws IOException, FileNotFoundException {
        for (String expression: expressao) {
            if (!validacao(tipoDadoEnum, expression)) return false;
        }
        
        return true;
    }
    
    private static boolean validacao(TipoDadoEnum tipoDadoEnum, String expressao) throws IOException, FileNotFoundException {
        String[] partes = Utilidades.retornaTresPartes(expressao);
        
        if (tipoDadoEnum != null) {
            switch (tipoDadoEnum) {
                case TEXTUAL: return validacaoTextual(partes);
                case INTEIRO: return validacaoInteiro(partes);
                case FRACIONARIO: return validacaoFracionario(partes);
                case DATA: return validacaoData(partes);
            }
        }
        
        return false;
    }
    
    private static boolean validacaoTextual(String[] expressao) throws IOException, FileNotFoundException {
        switch (expressao[1]) {
            case "Iguala":
                if (!validadorRegex(TipoDadoEnum.TEXTUAL, expressao[0]) || !validadorRegex(TipoDadoEnum.TEXTUAL, expressao[2])) return false;
                return expressao[0].equals(expressao[2]);
            case "Começadocom":
                if (!validadorRegex(TipoDadoEnum.TEXTUAL, expressao[0]) || !validadorRegex(TipoDadoEnum.TEXTUAL, expressao[2])) return false;
                return expressao[0].startsWith(expressao[2]);
            case "Terminadocom":
                if (!validadorRegex(TipoDadoEnum.TEXTUAL, expressao[0]) || !validadorRegex(TipoDadoEnum.TEXTUAL, expressao[2])) return false;
                return expressao[0].endsWith(expressao[2]);
            case "Contidoem":
                if (!validadorRegex(TipoDadoEnum.TEXTUAL, expressao[0]) || !validadorRegex(TipoDadoEnum.TEXTUAL, expressao[2])) return false;
                if (expressao[2].contains(";")) {
                    for (String expression: expressao[2].split(";")) {
                        if (expressao[0].equals(expression)) return true;
                    }
                } else if (expressao[2].contains("|")) {
                    for (String expression: expressao[2].split("|")) {
                        if (expressao[0].equals(expression)) return true;
                    }
                }
                break;
            case "Édotipotextual":
                if (validadorRegex(TipoDadoEnum.TEXTUAL, expressao[0])) return true;
                break;
            case "Édotipointeiro":
                if (validadorRegex(TipoDadoEnum.INTEIRO, expressao[0])) return true;
                break;
            case "Édotipofracionario":
                if (validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0])) return true;
                break;
            case "Édotipodata":
                if (validadorRegex(TipoDadoEnum.DATA, expressao[0])) return true;
                break;
        }
        
        return false;
    }
    
    private static boolean validacaoInteiro(String[] expressao) throws IOException, FileNotFoundException {
        int campo, valor;
        
        switch (expressao[1]) {
            case "Iguala":
                if (!validadorRegex(TipoDadoEnum.INTEIRO, expressao[0]) || !validadorRegex(TipoDadoEnum.INTEIRO, expressao[2])) return false;
                campo = Integer.parseInt(expressao[0]);
                valor = Integer.parseInt(expressao[2]);
                
                return campo == valor;
            case "Maiorque":
                if (!validadorRegex(TipoDadoEnum.INTEIRO, expressao[0]) || !validadorRegex(TipoDadoEnum.INTEIRO, expressao[2])) return false;
                campo = Integer.parseInt(expressao[0]);
                valor = Integer.parseInt(expressao[2]);
                
                return campo > valor;
            case "Menorque":
                if (!validadorRegex(TipoDadoEnum.INTEIRO, expressao[0]) || !validadorRegex(TipoDadoEnum.INTEIRO, expressao[2])) return false;
                campo = Integer.parseInt(expressao[0]);
                valor = Integer.parseInt(expressao[2]);
                
                return campo < valor;
            case "Contidoem":
                if (!validadorRegex(TipoDadoEnum.INTEIRO, expressao[0]) || !validadorRegex(TipoDadoEnum.INTEIRO, expressao[2])) return false;
                if (expressao[2].contains(";")) {
                    for (String expression: expressao[2].split(";")) {
                        if (expressao[0].equals(expression)) return true;
                    }
                } else if (expressao[2].contains("|")) {
                    for (String expression: expressao[2].split("|")) {
                        if (expressao[0].equals(expression)) return true;
                    }
                }
                break;
            case "Maiorouiguala":
                if (!validadorRegex(TipoDadoEnum.INTEIRO, expressao[0]) || !validadorRegex(TipoDadoEnum.INTEIRO, expressao[2])) return false;
                campo = Integer.parseInt(expressao[0]);
                valor = Integer.parseInt(expressao[2]);
                
                return campo >= valor;
            case "Menorouiguala":
                if (!validadorRegex(TipoDadoEnum.INTEIRO, expressao[0]) || !validadorRegex(TipoDadoEnum.INTEIRO, expressao[2])) return false;
                campo = Integer.parseInt(expressao[0]);
                valor = Integer.parseInt(expressao[2]);
                
                return campo <= valor;
            case "Diferente":
                if (!validadorRegex(TipoDadoEnum.INTEIRO, expressao[0]) || !validadorRegex(TipoDadoEnum.INTEIRO, expressao[2])) return false;
                campo = Integer.parseInt(expressao[0]);
                valor = Integer.parseInt(expressao[2]);
                
                return campo != valor;
            case "Édotipotextual":
                if (validadorRegex(TipoDadoEnum.TEXTUAL, expressao[0])) return true;
                break;
            case "Édotipointeiro":
                if (validadorRegex(TipoDadoEnum.INTEIRO, expressao[0])) return true;
                break;
            case "Édotipofracionario":
                if (validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0])) return true;
                break;
            case "Édotipodata":
                if (validadorRegex(TipoDadoEnum.DATA, expressao[0])) return true;
                break;
        }
        
        return false;
    }
    
    private static boolean validacaoFracionario(String[] expressao) throws IOException, FileNotFoundException {
        double campo, valor;
        
        switch (expressao[1]) {
            case "Casasdecimais":
                if (!validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0])) return false;
                if (!validadorRegex(TipoDadoEnum.INTEIRO, expressao[2])) return false;
                
                valor = Integer.parseInt(expressao[2]);
                
                if (expressao[0].contains(".")){
                    int qtdeCasas = expressao.length - expressao[0].lastIndexOf(".");
                    return valor == qtdeCasas;
                } else {
                    return valor == 0;
                }
            case "Iguala":
                expressao[0] = Utilidades.transformaParaFlutuante(expressao[0]);
                expressao[2] = Utilidades.transformaParaFlutuante(expressao[2]);
                
                if (!validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0]) || !validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[2])) return false;
                
                campo = Double.parseDouble(expressao[0]);
                valor = Double.parseDouble(expressao[2]);
                
                return campo == valor;
            case "Maiorque":
                expressao[0] = Utilidades.transformaParaFlutuante(expressao[0]);
                expressao[2] = Utilidades.transformaParaFlutuante(expressao[2]);
                
                if (!validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0]) || !validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[2])) return false;
                
                campo = Double.parseDouble(expressao[0]);
                valor = Double.parseDouble(expressao[2]);
                
                return campo > valor;
            case "Menorque":
                expressao[0] = Utilidades.transformaParaFlutuante(expressao[0]);
                expressao[2] = Utilidades.transformaParaFlutuante(expressao[2]);
                
                if (!validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0]) || !validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[2])) return false;
                
                campo = Double.parseDouble(expressao[0]);
                valor = Double.parseDouble(expressao[2]);
                
                return campo < valor;
            case "Contidoem":
                expressao[0] = Utilidades.transformaParaFlutuante(expressao[0]);
                expressao[2] = Utilidades.transformaParaFlutuante(expressao[2]);
                
                for (String expression: expressao[2].split(";")) {
                    if (expressao[0].contains(expression)) return true;
                }
                break;
            case "Maiorouiguala":
                expressao[0] = Utilidades.transformaParaFlutuante(expressao[0]);
                expressao[2] = Utilidades.transformaParaFlutuante(expressao[2]);
                
                if (!validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0]) || !validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[2])) return false;
                
                campo = Double.parseDouble(expressao[0]);
                valor = Double.parseDouble(expressao[2]);
                
                return campo >= valor;
            case "Menorouiguala":
                expressao[0] = Utilidades.transformaParaFlutuante(expressao[0]);
                expressao[2] = Utilidades.transformaParaFlutuante(expressao[2]);
                
                if (!validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0]) || !validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[2])) return false;
                
                campo = Double.parseDouble(expressao[0]);
                valor = Double.parseDouble(expressao[2]);
                
                return campo <= valor;
            case "Diferente":
                expressao[0] = Utilidades.transformaParaFlutuante(expressao[0]);
                expressao[2] = Utilidades.transformaParaFlutuante(expressao[2]);
                
                if (!validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0]) || !validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[2])) return false;
                
                campo = Double.parseDouble(expressao[0]);
                valor = Double.parseDouble(expressao[2]);
                
                return campo != valor;
            case "Édotipotextual":
                if (validadorRegex(TipoDadoEnum.TEXTUAL, expressao[0])) return true;
                break;
            case "Édotipointeiro":
                if (validadorRegex(TipoDadoEnum.INTEIRO, expressao[0])) return true;
                break;
            case "Édotipofracionario":
                if (validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0])) return true;
                break;
            case "Édotipodata":
                if (validadorRegex(TipoDadoEnum.DATA, expressao[0])) return true;
                break;
        }
        
        return false;
    }
    
    private static boolean validacaoData(String[] expressao) throws IOException, FileNotFoundException {
        SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");
        
        switch (expressao[1]) {
            case "Iguala":
                try {
                    Date data1 = formatadorData.parse(expressao[0]);
                    Date data2 = formatadorData.parse(expressao[2]);
                    
                    return data1.equals(data2);
                } catch (ParseException ex) {
                    return false;
                }
            case "Depoisde":
                try {
                    Date data1 = formatadorData.parse(expressao[0]);
                    Date data2 = formatadorData.parse(expressao[2]);
                    
                    return data1.after(data2);
                } catch (ParseException ex) {
                    return false;
                }
            case "Antesde":
                try {
                    Date data1 = formatadorData.parse(expressao[0]);
                    Date data2 = formatadorData.parse(expressao[2]);
                    
                    return data1.before(data2);
                } catch (ParseException ex) {
                    return false;
                }
            case "Formato":
                try {
                    Date data1 = formatadorData.parse(expressao[0]);
                    Date data2 = formatadorData.parse(expressao[2]);
                    
                    return true;
                } catch (ParseException ex) {
                    return false;
                }
            case "Contido em":
                try {
                    Date data1 = formatadorData.parse(expressao[0]);
                    String data2 = expressao[2];
                    
                    for (String expression: data2.split(";")) {
                        if (data1.equals(formatadorData.parse(expression))) return true;
                    }
                    break;
                } catch (ParseException ex) {
                    return false;
                }
            case "Édotipotextual":
                if (validadorRegex(TipoDadoEnum.TEXTUAL, expressao[0])) return true;
                break;
            case "Édotipointeiro":
                if (validadorRegex(TipoDadoEnum.INTEIRO, expressao[0])) return true;
                break;
            case "Édotipofracionario":
                if (validadorRegex(TipoDadoEnum.FRACIONARIO, expressao[0])) return true;
                break;
            case "Édotipodata":
                if (validadorRegex(TipoDadoEnum.DATA, expressao[0])) return true;
                break;
        }
        
        return false;
    }
    
}
