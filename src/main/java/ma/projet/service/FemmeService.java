package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class FemmeService implements IDao<Femme> {

    @Override
    public boolean create(Femme o) {
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
    public boolean update(Femme o) {
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
    public boolean delete(Femme o) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Femme managed = em.find(Femme.class, o.getId());
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
    public Femme findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Femme.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Femme> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM Femme f", Femme.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Afficher la femme la plus âgée
    public Femme afficherFemmePlusAgee() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            List<Femme> list = em.createQuery("SELECT f FROM Femme f ORDER BY f.dateNaissance ASC", Femme.class)
                                 .setMaxResults(1)
                                 .getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    // Nombre d'enfants d'une femme entre deux dates (requête native nommée)
    public int compterEnfantsEntreDates(Femme f, Date d1, Date d2) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Object res = em.createNamedQuery("Femme.countChildrenBetweenDates")
                           .setParameter("femmeId", f.getId())
                           .setParameter("d1", d1)
                           .setParameter("d2", d2)
                           .getSingleResult();
            if (res instanceof Number) {
                return ((Number) res).intValue();
            }
            return 0;
        } finally {
            em.close();
        }
    }

    // Femmes mariées au moins deux fois (requête nommée)
    public List<Femme> afficherFemmesMarieesAuMoinsDeuxFois() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createNamedQuery("Femme.findMarriedAtLeastTwice", Femme.class).getResultList();
        } finally {
            em.close();
        }
    }
}
