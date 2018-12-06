package controle.dataSimulation;

import dao.DaoOperacao;
import java.util.ArrayList;
import java.util.List;
import modelo.Operacao;
import modelo.TipoDadoEnum;
import util.JpaUtil;

public class PopulaOperacoes {
    
    public static void main(String[] args) {
        populaOperacoes();
    }
    
    private static void populaOperacoes() {
        List<Operacao> operacoes = new ArrayList<>();
        operacoes.add(new Operacao(TipoDadoEnum.TEXTUAL, true, "Igual a"));
        operacoes.add(new Operacao(TipoDadoEnum.TEXTUAL, true, "Começado com"));
        operacoes.add(new Operacao(TipoDadoEnum.TEXTUAL, true, "Terminado com"));
        operacoes.add(new Operacao(TipoDadoEnum.TEXTUAL, true, "Contido em"));
        operacoes.add(new Operacao(TipoDadoEnum.TEXTUAL, false, "Não permitir números"));
        operacoes.add(new Operacao(TipoDadoEnum.TEXTUAL, false, "É do tipo textual"));
        operacoes.add(new Operacao(TipoDadoEnum.TEXTUAL, false, "É do tipo inteiro"));
        operacoes.add(new Operacao(TipoDadoEnum.TEXTUAL, false, "É do tipo fracionário"));
        operacoes.add(new Operacao(TipoDadoEnum.TEXTUAL, false, "É do tipo data"));
        operacoes.add(new Operacao(TipoDadoEnum.INTEIRO, true, "Igual a"));
        operacoes.add(new Operacao(TipoDadoEnum.INTEIRO, true, "Maior que"));
        operacoes.add(new Operacao(TipoDadoEnum.INTEIRO, true, "Menor que"));
        operacoes.add(new Operacao(TipoDadoEnum.INTEIRO, true, "Contido em"));
        operacoes.add(new Operacao(TipoDadoEnum.INTEIRO, false, "É do tipo textual"));
        operacoes.add(new Operacao(TipoDadoEnum.INTEIRO, false, "É do tipo inteiro"));
        operacoes.add(new Operacao(TipoDadoEnum.INTEIRO, false, "É do tipo fracionário"));
        operacoes.add(new Operacao(TipoDadoEnum.INTEIRO, false, "É do tipo data"));
        operacoes.add(new Operacao(TipoDadoEnum.FRACIONARIO, true, "Igual a"));
        operacoes.add(new Operacao(TipoDadoEnum.FRACIONARIO, true, "Maior que"));
        operacoes.add(new Operacao(TipoDadoEnum.FRACIONARIO, true, "Menor que"));
        operacoes.add(new Operacao(TipoDadoEnum.FRACIONARIO, true, "Casas decimais"));
        operacoes.add(new Operacao(TipoDadoEnum.FRACIONARIO, true, "Contido em"));
        operacoes.add(new Operacao(TipoDadoEnum.FRACIONARIO, false, "É do tipo textual"));
        operacoes.add(new Operacao(TipoDadoEnum.FRACIONARIO, false, "É do tipo inteiro"));
        operacoes.add(new Operacao(TipoDadoEnum.FRACIONARIO, false, "É do tipo fracionário"));
        operacoes.add(new Operacao(TipoDadoEnum.FRACIONARIO, false, "É do tipo data"));
        operacoes.add(new Operacao(TipoDadoEnum.DATA, true, "Igual a"));
        operacoes.add(new Operacao(TipoDadoEnum.DATA, true, "Depois de"));
        operacoes.add(new Operacao(TipoDadoEnum.DATA, true, "Antes de"));
        operacoes.add(new Operacao(TipoDadoEnum.DATA, true, "Formato"));
        operacoes.add(new Operacao(TipoDadoEnum.DATA, true, "Contido em"));
        operacoes.add(new Operacao(TipoDadoEnum.DATA, false, "É do tipo textual"));
        operacoes.add(new Operacao(TipoDadoEnum.DATA, false, "É do tipo inteiro"));
        operacoes.add(new Operacao(TipoDadoEnum.DATA, false, "É do tipo fracionário"));
        operacoes.add(new Operacao(TipoDadoEnum.DATA, false, "É do tipo data"));
        
        DaoOperacao dao = new DaoOperacao();
        
        operacoes.forEach((operacao) -> {
            dao.add(operacao);
        });
        
        JpaUtil.closeEntityManagerFactory();
    }
    
}