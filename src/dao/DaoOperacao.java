package dao;

import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import modelo.Operacao;
import modelo.TipoDadoEnum;
import org.hibernate.exception.ConstraintViolationException;

public class DaoOperacao {
    
    public void add (Operacao operacao) 
            throws IllegalStateException, EntityExistsException, 
                   PersistenceException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        dao.add(operacao);
    }
    
    public void remove (Operacao operacao) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        dao.remove(operacao);
    }
    
    public void update (Operacao operacao) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException,
                   ConstraintViolationException {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        dao.update(operacao);
    }
    
    public Operacao findOperacaoById(long id) {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        return dao.findObjectById(id);
    }
    
    public List<Operacao> findOperacoes(String collumn, String search) {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        return dao.findObjects(collumn, search);
    }
    
    public List<Operacao> findOperacoesByNumber(String collumn, long number) {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Operacao> findOperacoesByNumber(String collumn, int number) {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Operacao> findOperacoesByNumber(String collumn, float number) {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Operacao> findOperacoesByNumber(String collumn, double number) {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Operacao> findOperacoesByTipoDado(TipoDadoEnum tipoDado) {
        DaoGenerico<Operacao> dao = new DaoGenerico<>(Operacao.class);
        return dao.findObjectsByEnum("tipoDado", tipoDado);
    }
    
}