package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.List;

public class ProjetService implements IDao<Projet> {

    @Override
    public boolean create(Projet o) {
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
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Projet managed = em.find(Projet.class, o.getId());
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
    public Projet findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Projet.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Projet> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Projet p", Projet.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Afficher la liste des tâches planifiées pour un projet
    public List<Tache> afficherTachesPlanifiees(Projet p) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tache t WHERE t.projet.id = :pId", Tache.class)
                     .setParameter("pId", p.getId())
                     .getResultList();
        } finally {
            em.close();
        }
    }

    // Afficher la liste des tâches réalisées avec les dates réelles (format demandé)
    public void afficherTachesRealisees(Projet p) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Projet proj = em.find(Projet.class, p.getId());
            SimpleDateFormat sdfMois = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat sdfCourt = new SimpleDateFormat("dd/MM/yyyy");
            String dateDebutStr = proj.getDateDebut() != null ? sdfMois.format(proj.getDateDebut()) : "";

            System.out.println("Projet : " + proj.getId() + "      Nom : " + proj.getNom() + "     Date début : " + dateDebutStr);
            System.out.println("Liste des tâches:");
            System.out.printf("%-4s%-15s%-20s%-20s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");

            String jpql = "SELECT et FROM EmployeTache et JOIN FETCH et.tache WHERE et.tache.projet.id = :pId AND et.dateFinReelle IS NOT NULL";
            List<EmployeTache> list = em.createQuery(jpql, EmployeTache.class)
                                        .setParameter("pId", proj.getId())
                                        .getResultList();
            for (EmployeTache et : list) {
                System.out.printf("%-4d%-15s%-20s%-20s%n",
                        et.getTache().getId(),
                        et.getTache().getNom(),
                        sdfCourt.format(et.getDateDebutReelle()),
                        sdfCourt.format(et.getDateFinReelle()));
            }
        } finally {
            em.close();
        }
    }
}
