package dao;

import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import modelo.Condicao;
import modelo.Regra;
import org.hibernate.exception.ConstraintViolationException;
import util.JpaUtil;

public class DaoCondicao {
    
    public void add (Condicao condicao) 
            throws IllegalStateException, EntityExistsException, 
                   PersistenceException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException {
        DaoGenerico<Condicao> dao = new DaoGenerico<>(Condicao.class);
        dao.add(condicao);
    }
    
    public void remove (Condicao condicao) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException {
        DaoGenerico<Condicao> dao = new DaoGenerico<>(Condicao.class);
        dao.remove(condicao);
    }
    
    public void update (Condicao condicao) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException,
                   ConstraintViolationException {
        DaoGenerico<Condicao> dao = new DaoGenerico<>(Condicao.class);
        dao.update(condicao);
    }
    
    public void update (Regra regra, List<Condicao> condicoes) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException,
                   ConstraintViolationException {
        EntityManager manager = JpaUtil.getEntityManager();
        
        manager.getTransaction().begin();
        
        manager.merge(regra);
        
        findCondicoesByReference(regra).forEach(condicao -> {
            manager.remove(condicao);
        });
        
        condicoes.forEach(condicao -> {
            manager.persist(condicao);
        });
        
        manager.getTransaction().commit();
        
        JpaUtil.closeEntityManagerFactory();
    }
    
    public void remove (Regra regra, List<Condicao> condicoes) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException,
                   ConstraintViolationException {
        EntityManager manager = JpaUtil.getEntityManager();
        
        manager.getTransaction().begin();
        
        condicoes.forEach(condicao -> {
            manager.remove(condicao);
        });
        
        manager.merge(regra);
        
        manager.getTransaction().commit();
        
        manager.close();
    }
    
    public void remove (List<Condicao> condicoes) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException,
                   ConstraintViolationException {
        EntityManager manager = JpaUtil.getEntityManager();
        
        manager.getTransaction().begin();
        
        condicoes.forEach(condicao -> {
            manager.remove(condicao);
        });
        
        manager.getTransaction().commit();
    }
    
    public void remove (Regra regra) 
            throws IllegalStateException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException,
                   ConstraintViolationException {
        EntityManager manager = JpaUtil.getEntityManager();
        
        manager.getTransaction().begin();
        
        List<Condicao> condicoes = findCondicoesByReference(regra);
        
        condicoes.forEach(condicao -> {
            manager.remove(condicao);
        });
        
        manager.getTransaction().commit();
    }
    
    public void add (List<Condicao> condicoes) 
            throws IllegalStateException, EntityExistsException, 
                   PersistenceException, IllegalArgumentException,
                   TransactionRequiredException, RollbackException {
        EntityManager manager = JpaUtil.getEntityManager();
        
        manager.getTransaction().begin();
        condicoes.forEach(condicao -> {
            manager.persist(condicao);
        });
        manager.getTransaction().commit();
    }
    
    public Condicao findCondicaoById(long id) {
        DaoGenerico<Condicao> dao = new DaoGenerico<>(Condicao.class);
        return dao.findObjectById(id);
    }
    
    public List<Condicao> findCondicoes(String collumn, String search) {
        DaoGenerico<Condicao> dao = new DaoGenerico<>(Condicao.class);
        return dao.findObjects(collumn, search);
    }
    
    public List<Condicao> findCondicoesByNumber(String collumn, long number) {
        DaoGenerico<Condicao> dao = new DaoGenerico<>(Condicao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Condicao> findCondicoesByNumber(String collumn, int number) {
        DaoGenerico<Condicao> dao = new DaoGenerico<>(Condicao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Condicao> findCondicoesByNumber(String collumn, float number) {
        DaoGenerico<Condicao> dao = new DaoGenerico<>(Condicao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Condicao> findCondicoesByNumber(String collumn, double number) {
        DaoGenerico<Condicao> dao = new DaoGenerico<>(Condicao.class);
        return dao.findObjectsByNumber(collumn, number);
    }
    
    public List<Condicao> findCondicoesByReference(Regra reference) {
        TypedQuery<Condicao> query = JpaUtil.getEntityManager().createQuery("SELECT c FROM Condicao c WHERE c.regra = :reference", Condicao.class);
        query.setParameter("reference", reference);
        return query.getResultList();
    }
    
}