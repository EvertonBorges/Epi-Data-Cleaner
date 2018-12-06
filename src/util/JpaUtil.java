package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaUtil {

    private static final String PERSISTENCE_UNIT = "Epi_Data_CleanerPU";
    private static final ThreadLocal<EntityManager> THREAD_LOCAL = new ThreadLocal<>();
    private static EntityManagerFactory entityManagerFactory;
    
    private JpaUtil() {
    }
    
    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        EntityManager entityManager = THREAD_LOCAL.get();
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
            JpaUtil.THREAD_LOCAL.set(entityManager);
        }
        return entityManager;
    }
    
    public static boolean existEntityManager() {
        return THREAD_LOCAL.get() != null;
    }
    
    public static boolean existEntityManagerFactory() {
        return entityManagerFactory != null;
    }

    public static void closeEntityManager() {
        EntityManager em = THREAD_LOCAL.get();
        if (em != null) {
            EntityTransaction transaction = em.getTransaction();
            if (transaction.isActive()) {
                transaction.commit();
            }
            em.close();
            THREAD_LOCAL.set(null);
        }
    }

    public static void closeEntityManagerFactory() {
        closeEntityManager();
        entityManagerFactory.close();
        entityManagerFactory = null;
    }
        
}