package dao;

import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import modelo.Campo;
import org.hibernate.exception.ConstraintViolationException;

public class DaoCampo {
    
    public void add (Campo campo) 
            throws IllegalStateException, EntityExistsException, 
                   PersistenceException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException {
        DaoGenerico<Campo> dao = new DaoGenerico<>(Campo.class);
        dao.add(campo);
    }
    
    public void remove (Campo campo) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException{
        DaoGenerico<Campo> dao = new DaoGenerico<>(Campo.class);
        dao.remove(campo);
    }
    
    public void update (Campo campo) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException,
                   ConstraintViolationException {
        DaoGenerico<Campo> dao = new DaoGenerico<>(Campo.class);
        dao.update(campo);
    }
    
    public Campo findCamposById(long id) {
        DaoGenerico<Campo> dao = new DaoGenerico<>(Campo.class);
        return dao.findObjectById(id);
    }
    
    public List<Campo> findCampos(String collumn, String search) {
        DaoGenerico<Campo> dao = new DaoGenerico<>(Campo.class);
        return dao.findObjects(collumn, search);
    }
    
    public List<Campo> findCamposByNumber(String collumn, long number) {
        DaoGenerico<Campo> dao = new DaoGenerico<>(Campo.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Campo> findCamposByNumber(String collumn, int number) {
        DaoGenerico<Campo> dao = new DaoGenerico<>(Campo.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Campo> findCamposByNumber(String collumn, float number) {
        DaoGenerico<Campo> dao = new DaoGenerico<>(Campo.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Campo> findCamposByNumber(String collumn, double number) {
        DaoGenerico<Campo> dao = new DaoGenerico<>(Campo.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
}