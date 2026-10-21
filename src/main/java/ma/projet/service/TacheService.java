package ma.projet.service;

import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class TacheService implements IDao<Tache> {

    @Override
    public boolean create(Tache o) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(o);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(Tache o) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(o);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Tache o) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Tache managed = em.find(Tache.class, o.getId());
            if (managed != null) em.remove(managed);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Tache findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Tache.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Tache> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tache t", Tache.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Tâches dont le prix > 1000 DH (requête nommée)
    public List<Tache> afficherTachesPrixSuperieur1000() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createNamedQuery("Tache.findByPriceAbove1000", Tache.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Tâches réalisées entre deux dates
    public List<Tache> afficherTachesRealiseesEntreDates(Date d1, Date d2) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT et.tache FROM EmployeTache et WHERE et.dateDebutReelle >= :d1 AND et.dateFinReelle <= :d2";
            return em.createQuery(jpql, Tache.class)
                     .setParameter("d1", d1)
                     .setParameter("d2", d2)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
