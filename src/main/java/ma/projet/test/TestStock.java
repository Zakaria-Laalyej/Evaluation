package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestStock {

    public static void executer() throws Exception {
        System.out.println("=================================================================");
        System.out.println("   EXERCICE 1 : APPLICATION DE GESTION DE STOCK");
        System.out.println("=================================================================");

        CategorieService catService = new CategorieService();
        ProduitService prodService = new ProduitService();
        CommandeService cmdService = new CommandeService();
        LigneCommandeService ligneService = new LigneCommandeService();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // 1. Catégories
        Categorie c1 = new Categorie("INFO", "Informatique");
        catService.create(c1);

        // 2. Produits
        Produit p1 = new Produit("ES12", 170.0f, sdf.parse("01/01/2013"), c1);
        Produit p2 = new Produit("ZR85", 100.0f, sdf.parse("05/01/2013"), c1);
        Produit p3 = new Produit("EE85", 200.0f, sdf.parse("10/01/2013"), c1);
        prodService.create(p1);
        prodService.create(p2);
        prodService.create(p3);

        // 3. Commande
        Commande cmd4 = new Commande(sdf.parse("14/03/2013"));
        cmdService.create(cmd4);

        // 4. Lignes de commande
        LigneCommandeProduit l1 = new LigneCommandeProduit(p1, cmd4, 7);
        LigneCommandeProduit l2 = new LigneCommandeProduit(p2, cmd4, 14);
        LigneCommandeProduit l3 = new LigneCommandeProduit(p3, cmd4, 5);
        ligneService.create(l1);
        ligneService.create(l2);
        ligneService.create(l3);

        // Affichage demandé pour la commande
        System.out.println("\n-> Affichage des produits de la commande 4 (format exact) :");
        prodService.afficherProduitsDuneCommande(cmd4);

        // Produits prix > 100 DH avec requête nommée
        System.out.println("\n-> Produits avec prix > 100 DH (Requête Nommée) :");
        List<Produit> chers = prodService.afficherProduitsPrixSuperieur100();
        chers.forEach(p -> System.out.println("   * Ref: " + p.getReference() + " | Prix: " + p.getPrix() + " DH"));

        // Produits commandés entre deux dates
        System.out.println("\n-> Produits commandés entre 01/03/2013 et 31/03/2013 :");
        List<Produit> cmdProds = prodService.afficherProduitsCommandesEntreDates(sdf.parse("01/03/2013"), sdf.parse("31/03/2013"));
        cmdProds.forEach(p -> System.out.println("   * " + p.getReference()));
    }
}
