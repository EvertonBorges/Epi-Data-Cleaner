package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.Arquivo;
import modelo.Campo;
import util.JpaUtil;

public class DaoArquivo {
    
    public void add (Arquivo arquivo) {
        DaoGenerico<Arquivo> dao = new DaoGenerico<>(Arquivo.class);
        dao.add(arquivo);
    }
    
    public void remove (Arquivo arquivo) {
        DaoGenerico<Arquivo> dao = new DaoGenerico<>(Arquivo.class);
        dao.remove(arquivo);
    }
    
    public void update (Arquivo arquivo) {
        /*DaoGenerico<Arquivo> dao = new DaoGenerico<>(Arquivo.class);
        dao.update(arquivo);*/
        update(arquivo, arquivo.getCampos());
    }
    
    private void update (Arquivo arquivo, List<Campo> campos) {
        EntityManager manager = JpaUtil.getEntityManager();
        
        manager.getTransaction().begin();
        
        manager.merge(arquivo);
        
        if (!arquivo.getCampos().equals(campos)) {
            arquivo.getCampos().forEach(campo -> {
                campo.getAcoes().forEach(acao -> {
                    manager.remove(manager.getReference(acao.getClass(), acao.getId()));
                });
                campo.getAcoes().clear();
                manager.remove(manager.getReference(campo.getClass(), campo.getId()));
            });

            arquivo.setCampos(campos);

            campos.forEach(campo -> {
                campo.setArquivo(arquivo);
                manager.persist(campo);
            });
        }
        
        manager.getTransaction().commit();
        
        JpaUtil.closeEntityManagerFactory();
    }
    
    private void update (List<Campo> campos) {
        
    }
    
    public Arquivo findArquivoById(long id) {
        DaoGenerico<Arquivo> dao = new DaoGenerico<>(Arquivo.class);
        return dao.findObjectById(id);
    }
    
    public List<Arquivo> findArquivo(String collumn, String search) {
        DaoGenerico<Arquivo> dao = new DaoGenerico<>(Arquivo.class);
        return dao.findObjects(collumn, search);
    }
    
    public List<Campo> findCamposByReference(Arquivo reference) {
        TypedQuery<Campo> query = JpaUtil.getEntityManager().createQuery("SELECT c FROM Campo c WHERE c.arquivo = :reference", Campo.class);
        query.setParameter("reference", reference);
        return query.getResultList();
    }
    
    public Arquivo findArquivoByPath(String path) {
        DaoGenerico<Arquivo> dao = new DaoGenerico<>(Arquivo.class);
        String caminho = path.substring(0, path.lastIndexOf("\\"));
        String nomeArquivo = path.substring(path.lastIndexOf("\\") + 1);
        
        caminho = caminho.replace("\\", "\\\\\\\\");
        
        System.out.println("Camiho = " + caminho);
        
        String sql = "SELECT a FROM Arquivo a WHERE a.caminho LIKE '" + caminho + "' AND a.nome LIKE '" + nomeArquivo + "'";
        
        return dao.findObject(sql);
    }
    
}