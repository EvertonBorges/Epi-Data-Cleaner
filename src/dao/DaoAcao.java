package dao;

import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import modelo.Acao;
import modelo.Campo;
import org.hibernate.exception.ConstraintViolationException;
import util.JpaUtil;

public class DaoAcao {
    
    public void add (Acao acao) 
            throws IllegalStateException, EntityExistsException, 
                   PersistenceException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException {
        DaoGenerico<Acao> dao = new DaoGenerico<>(Acao.class);
        dao.add(acao);
    }
    
    public void remove (Acao acao) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException{
        DaoGenerico<Acao> dao = new DaoGenerico<>(Acao.class);
        dao.remove(acao);
    }
    
    public void update (Acao acao) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException,
                   ConstraintViolationException {
        DaoGenerico<Acao> dao = new DaoGenerico<>(Acao.class);
        dao.update(acao);
    }
    
    public Acao findAcoesById(long id) {
        DaoGenerico<Acao> dao = new DaoGenerico<>(Acao.class);
        return dao.findObjectById(id);
    }
    
    public List<Acao> findAcoes(String collumn, String search) {
        DaoGenerico<Acao> dao = new DaoGenerico<>(Acao.class);
        return dao.findObjects(collumn, search);
    }
    
    public List<Acao> findAcoesByNumber(String collumn, long number) {
        DaoGenerico<Acao> dao = new DaoGenerico<>(Acao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Acao> findAcoesByNumber(String collumn, int number) {
        DaoGenerico<Acao> dao = new DaoGenerico<>(Acao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Acao> findAcoesByNumber(String collumn, float number) {
        DaoGenerico<Acao> dao = new DaoGenerico<>(Acao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Acao> findAcoesByNumber(String collumn, double number) {
        DaoGenerico<Acao> dao = new DaoGenerico<>(Acao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Acao> findAcoesByReference(Campo reference) {
        TypedQuery<Acao> query = JpaUtil.getEntityManager().createQuery("SELECT a FROM Acao a WHERE a.campo = :reference ORDER BY a.id", Acao.class);
        query.setParameter("reference", reference);
        return query.getResultList();
    }
    
}