package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class EmployeService implements IDao<Employe> {

    @Override
    public boolean create(Employe o) {
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
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Employe managed = em.find(Employe.class, o.getId());
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
    public Employe findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Employe.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Employe> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Employe e", Employe.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Afficher la liste des tâches réalisées par un employé
    public List<Tache> afficherTachesParEmploye(Employe e) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String jpql = "SELECT et.tache FROM EmployeTache et WHERE et.employe.id = :empId AND et.dateFinReelle IS NOT NULL";
            return em.createQuery(jpql, Tache.class)
                     .setParameter("empId", e.getId())
                     .getResultList();
        } finally {
            em.close();
        }
    }

    // Afficher la liste des projets gérés par un employé
    public List<Projet> afficherProjetsParEmploye(Employe e) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Projet p WHERE p.responsable.id = :empId", Projet.class)
                     .setParameter("empId", e.getId())
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
