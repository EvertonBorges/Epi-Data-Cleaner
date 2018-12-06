package controle.gerenciar;

public class VisaoPrevia {
    
    private long line;
    private String isValidated;
    private String value;
    private String valueSugested;

    public VisaoPrevia() {
        
    }

    public VisaoPrevia(long line, String isValidated, String value, String valueSugested) {
        this.line = line;
        this.isValidated = isValidated;
        this.value = value;
        this.valueSugested = valueSugested;
    }

    public long getLine() {
        return line;
    }

    public void setLine(long line) {
        this.line = line;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueSugested() {
        return valueSugested;
    }

    public void setValueSugested(String valueSugested) {
        this.valueSugested = valueSugested;
    }
    
}