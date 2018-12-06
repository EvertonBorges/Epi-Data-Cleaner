package util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Serializer_Desserializer {
    
    public Serializer_Desserializer() {
        
    }
    
    public void serializeWithXML(Class classe, Object objeto) throws FileNotFoundException {
        FileOutputStream saida = new FileOutputStream(classe.getSimpleName() + ".xml");
        
        try (XMLEncoder serializador = new XMLEncoder(saida)) {
            serializador.writeObject(classe.cast(objeto));
        }
    }
    
    public Object desserializerWithXML(Class classe) throws FileNotFoundException {
        FileInputStream entrada = new FileInputStream(classe.getSimpleName() + ".xml");
        XMLDecoder desserializador = new XMLDecoder(entrada);
        
        return desserializador.readObject();
    }
}