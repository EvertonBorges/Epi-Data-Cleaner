package dao;

import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import modelo.Regra;
import modelo.TipoDadoEnum;
import org.hibernate.exception.ConstraintViolationException;
import util.JpaUtil;

public class DaoRegra {
    
    public void add (Regra regra) 
            throws IllegalStateException, EntityExistsException, 
                   PersistenceException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException {
        DaoGenerico<Regra> dao = new DaoGenerico<>(Regra.class);
        dao.add(regra);
    }
    
    public void remove (Regra regra) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException{
        DaoGenerico<Regra> dao = new DaoGenerico<>(Regra.class);
        dao.remove(regra);
    }
    
    public void update (Regra regra) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException,
                   ConstraintViolationException {
        DaoGenerico<Regra> dao = new DaoGenerico<>(Regra.class);
        dao.update(regra);
    }
    
    public Regra findRegrasById(long id) {
        DaoGenerico<Regra> dao = new DaoGenerico<>(Regra.class);
        return dao.findObjectById(id);
    }
    
    public List<Regra> findRegras(String collumn, String search) {
        DaoGenerico<Regra> dao = new DaoGenerico<>(Regra.class);
        return dao.findObjects(collumn, search);
    }
    
    public List<Regra> findRegrasByNumber(String collumn, long number) {
        DaoGenerico<Regra> dao = new DaoGenerico<>(Regra.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Regra> findRegrasByNumber(String collumn, int number) {
        DaoGenerico<Regra> dao = new DaoGenerico<>(Regra.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Regra> findRegrasByNumber(String collumn, float number) {
        DaoGenerico<Regra> dao = new DaoGenerico<>(Regra.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Regra> findRegrasByNumber(String collumn, double number) {
        DaoGenerico<Regra> dao = new DaoGenerico<>(Regra.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Regra> findRegrasByReference(TipoDadoEnum reference) {
        TypedQuery<Regra> query = JpaUtil.getEntityManager().createQuery("SELECT r FROM Regra r WHERE r.tipoDado = :reference ORDER BY r.id", Regra.class);
        query.setParameter("reference", reference);
        return query.getResultList();
    }
    
}