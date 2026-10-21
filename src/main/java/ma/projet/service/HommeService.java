package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme o) {
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
    public boolean update(Homme o) {
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
    public boolean delete(Homme o) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Homme managed = em.find(Homme.class, o.getId());
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
    public Homme findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Homme.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Homme> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT h FROM Homme h", Homme.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Afficher les épouses d'un homme entre deux dates
    public List<Femme> afficherEpousesEntreDates(Homme h, Date d1, Date d2) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String jpql = "SELECT m.femme FROM Mariage m WHERE m.homme.id = :hommeId AND m.dateDebut BETWEEN :d1 AND :d2";
            return em.createQuery(jpql, Femme.class)
                     .setParameter("hommeId", h.getId())
                     .setParameter("d1", d1)
                     .setParameter("d2", d2)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    // Afficher le nombre d'hommes mariés à quatre femmes entre deux dates (API Criteria)
    public long compterHommesMariesQuatreFemmes(Date d1, Date d2) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Mariage> mariage = cq.from(Mariage.class);

            cq.select(cb.countDistinct(mariage.get("homme")));
            Predicate datePredicate = cb.between(mariage.get("dateDebut"), d1, d2);
            cq.where(datePredicate);
            cq.groupBy(mariage.get("homme"));
            cq.having(cb.ge(cb.count(mariage), 4L));

            List<Long> results = em.createQuery(cq).getResultList();
            return results.size();
        } finally {
            em.close();
        }
    }

    // Afficher les mariages d'un homme donné avec affichage détaillé (format demandé)
    public void afficherMariagesHomme(Homme h) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Homme homme = em.find(Homme.class, h.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            System.out.println("Nom : " + (homme.getNom() + " " + homme.getPrenom()).toUpperCase());

            String jpql = "SELECT m FROM Mariage m JOIN FETCH m.femme WHERE m.homme.id = :hId ORDER BY m.dateDebut ASC";
            List<Mariage> mariages = em.createQuery(jpql, Mariage.class)
                                       .setParameter("hId", homme.getId())
                                       .getResultList();

            List<Mariage> enCours = new ArrayList<>();
            List<Mariage> echoues = new ArrayList<>();

            for (Mariage m : mariages) {
                if (m.getDateFin() == null) {
                    enCours.add(m);
                } else {
                    echoues.add(m);
                }
            }

            System.out.println("Mariages En Cours :");
            int i = 1;
            for (Mariage m : enCours) {
                System.out.printf("%d. Femme : %-15s Date Début : %-12s Nbr Enfants : %d%n",
                        i++,
                        (m.getFemme().getNom() + " " + m.getFemme().getPrenom()).toUpperCase(),
                        sdf.format(m.getDateDebut()),
                        m.getNbrEnfants());
            }

            System.out.println("\nMariages échoués :");
            int j = 1;
            for (Mariage m : echoues) {
                System.out.printf("%d. Femme : %-15s Date Début : %-12s Date Fin : %-12s Nbr Enfants : %d%n",
                        j++,
                        (m.getFemme().getNom() + " " + m.getFemme().getPrenom()).toUpperCase(),
                        sdf.format(m.getDateDebut()),
                        sdf.format(m.getDateFin()),
                        m.getNbrEnfants());
            }
        } finally {
            em.close();
        }
    }
}
