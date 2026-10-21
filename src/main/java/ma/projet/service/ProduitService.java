package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProduitService implements IDao<Produit> {

    @Override
    public boolean create(Produit o) {
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
    public boolean update(Produit o) {
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
    public boolean delete(Produit o) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Produit managed = em.find(Produit.class, o.getId());
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
    public Produit findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Produit.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Produit> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produit p", Produit.class).getResultList();
        } finally {
            em.close();
        }
    }

    // 1. Liste des produits par catégorie
    public List<Produit> afficherProduitsParCategorie(Categorie c) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produit p WHERE p.categorie.id = :catId", Produit.class)
                     .setParameter("catId", c.getId())
                     .getResultList();
        } finally {
            em.close();
        }
    }

    // 2. Produits commandés entre deux dates
    public List<Produit> afficherProduitsCommandesEntreDates(Date d1, Date d2) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT l.produit FROM LigneCommandeProduit l WHERE l.commande.date BETWEEN :d1 AND :d2";
            return em.createQuery(jpql, Produit.class)
                     .setParameter("d1", d1)
                     .setParameter("d2", d2)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    // 3. Produits commandés dans une commande donnée avec affichage formaté
    public void afficherProduitsDuneCommande(Commande c) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Commande cmd = em.find(Commande.class, c.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            String dateStr = cmd.getDate() != null ? sdf.format(cmd.getDate()) : "";

            System.out.println("Commande : " + cmd.getId() + "     Date : " + dateStr);
            System.out.println("Liste des produits :");
            System.out.printf("%-12s%-10s%-10s%n", "Référence", "Prix", "Quantité");

            String jpql = "SELECT l FROM LigneCommandeProduit l JOIN FETCH l.produit WHERE l.commande.id = :cmdId";
            List<LigneCommandeProduit> lignes = em.createQuery(jpql, LigneCommandeProduit.class)
                                                  .setParameter("cmdId", cmd.getId())
                                                  .getResultList();
            for (LigneCommandeProduit l : lignes) {
                System.out.printf("%-12s%-10s%-10d%n",
                        l.getProduit().getReference(),
                        (int) l.getProduit().getPrix() + " DH",
                        l.getQuantite());
            }
        } finally {
            em.close();
        }
    }

    // 4. Produits dont le prix > 100 DH avec requête nommée
    public List<Produit> afficherProduitsPrixSuperieur100() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createNamedQuery("Produit.findByPriceAbove100", Produit.class).getResultList();
        } finally {
            em.close();
        }
    }
}
