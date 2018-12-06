package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import util.JpaUtil;

public class DaoGenerico<Type> {
    
    private final Class classe;
    private static final String ROLLBACK_TITULO = "ERRO";
    private static final String ROLLBACK_CABECALHO = "ROLLBACK";
    private static final String ROLLBACK_CORPO = "Erro durante a persistência dos dados. Foi dado um Rollback nas informações.";

    public DaoGenerico(Class classe) {
        this.classe = classe;
    }
    
    public void add (Type instance) {
        EntityManager manager = JpaUtil.getEntityManager();
        
        try {
            manager.getTransaction().begin();
            manager.persist(instance);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            //manager.getTransaction().rollback();
            
            AlertaRollBack(ex);
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }
    
    public void remove (Type instance) {
        EntityManager manager = JpaUtil.getEntityManager();
        
        try {
            manager.getTransaction().begin();
            manager.remove(instance);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            //manager.getTransaction().rollback();
            
            AlertaRollBack(ex);
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }
    
    public void update (Type instance) {
        EntityManager manager = JpaUtil.getEntityManager();
        
        try {
            manager.getTransaction().begin();
            manager.merge(instance);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            
            //manager.getTransaction().rollback();
            
            AlertaRollBack(ex);
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }
    
    public Type findObjectById(long id) {
        return (Type) JpaUtil.getEntityManager().find(classe, id);
    }
    
    public List<Type> findObjects(String sql) {
        TypedQuery<Type> query = JpaUtil.getEntityManager().createQuery(sql, classe);
        return query.getResultList();
    }
    
    public Type findObject(String sql) {
        TypedQuery<Type> query = JpaUtil.getEntityManager().createQuery(sql, classe);
        return query.getSingleResult();
    }
    
    public List<Type> findObjects(String collumn, String search) {
        boolean isFilter = (collumn.compareTo("") != 0);
        String textSearch = "SELECT o FROM " + classe.getSimpleName() + " o";
        if (isFilter) {
            textSearch += " WHERE o." + collumn + " LIKE :search";
        }
        
        TypedQuery<Type> query = JpaUtil.getEntityManager().createQuery(textSearch, classe);
        if (isFilter) {
            query.setParameter("search", search + "%");
        }
        
        return query.getResultList();
    }
    
    public List<Type> findObjectsByNumber(String collumn, long number) {
        boolean isFilter = (collumn.compareTo("") != 0);
        String textSearch = "SELECT o FROM " + classe.getSimpleName() + " o";
        if (isFilter) {
            textSearch += " WHERE o." + collumn + " = :search";
        }
        
        TypedQuery<Type> query = JpaUtil.getEntityManager().createQuery(textSearch, classe);
        if (isFilter) {
            query.setParameter("search", number);
        }
        
        return query.getResultList();
    }
    
    public List<Type> findObjectsByNumber(String collumn, int number) {
        boolean isFilter = (collumn.compareTo("") != 0);
        String textSearch = "SELECT o FROM " + classe.getSimpleName() + " o";
        if (isFilter) {
            textSearch += " WHERE o." + collumn + " = :search";
        }
        
        TypedQuery<Type> query = JpaUtil.getEntityManager().createQuery(textSearch, classe);
        if (isFilter) {
            query.setParameter("search", number);
        }
        
        return query.getResultList();
    }
    
    public List<Type> findObjectsByNumber(String collumn, double number) {
        boolean isFilter = (collumn.compareTo("") != 0);
        String textSearch = "SELECT o FROM " + classe.getSimpleName() + " o";
        if (isFilter) {
            textSearch += " WHERE o." + collumn + " = :search";
        }
        
        TypedQuery<Type> query = JpaUtil.getEntityManager().createQuery(textSearch, classe);
        if (isFilter) {
            query.setParameter("search", number);
        }
        
        return query.getResultList();
    }
    
    public List<Type> findObjectsByNumber(String collumn, float number) {
        boolean isFilter = (collumn.compareTo("") != 0);
        String textSearch = "SELECT o FROM " + classe.getSimpleName() + " o";
        if (isFilter) {
            textSearch += " WHERE o." + collumn + " = :search";
        }
        
        TypedQuery<Type> query = JpaUtil.getEntityManager().createQuery(textSearch, classe);
        if (isFilter) {
            query.setParameter("search", number);
        }
        
        return query.getResultList();
    }
    
    public List<Type> findObjectsByEnum(String collumn, Enum number) {
        boolean isFilter = (collumn.compareTo("") != 0);
        String textSearch = "SELECT o FROM " + classe.getSimpleName() + " o";
        if (isFilter) {
            textSearch += " WHERE o." + collumn + " = :search";
        }
        
        TypedQuery<Type> query = JpaUtil.getEntityManager().createQuery(textSearch, classe);
        if (isFilter) {
            query.setParameter("search", number);
        }
        
        return query.getResultList();
    }
    
    private void AlertaRollBack(Exception ex) {
        util.Utilidades.AlertInformation(ROLLBACK_TITULO, ROLLBACK_CABECALHO, util.Utilidades.corpoMensagem(ROLLBACK_CORPO, ex));
    }
    
}
