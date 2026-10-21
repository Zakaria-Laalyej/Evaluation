package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestEtatCivil {

    public static void executer() throws Exception {
        System.out.println("\n=================================================================");
        System.out.println("   EXERCICE 3 : APPLICATION DE GESTION DE L'ÉTAT CIVIL");
        System.out.println("=================================================================");

        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // 1. Création de 5 hommes
        Homme h1 = new Homme("Wahbi", "Said", "0611111111", "Casablanca", sdf.parse("10/05/1965"));
        Homme h2 = new Homme("Berrada", "Mehdi", "0642272222", "Meknes", sdf.parse("15/08/1970"));
        Homme h3 = new Homme("Tazi", "Hassan", "0639333333", "Fès", sdf.parse("20/11/1975"));
        Homme h4 = new Homme("El Amrani", "Youssef", "0644444444", "Marrakech", sdf.parse("05/01/1984"));
        Homme h5 = new Homme("Menssouri", "Hatim", "0655556555", "Tanger", sdf.parse("12/03/1995"));

        hommeService.create(h1);
        hommeService.create(h2);
        hommeService.create(h3);
        hommeService.create(h4);
        hommeService.create(h5);

        // 2. Création de 10 femmes
        Femme f1 = new Femme("Rami", "Salima", "0660000001", "Casablanca", sdf.parse("01/01/1972"));
        Femme f2 = new Femme("Ali", "Amal", "0660000002", "Casablanca", sdf.parse("14/02/1974"));
        Femme f3 = new Femme("Alaoui", "Wafa", "0660000003", "Casablanca", sdf.parse("22/07/1978"));
        Femme f4 = new Femme("Alami", "Karima", "0660000004", "Casablanca", sdf.parse("05/09/1968"));
        Femme f5 = new Femme("Benjelloun", "Nadia", "0660000005", "Rabat", sdf.parse("12/12/1970"));
        Femme f6 = new Femme("Filali", "Meryem", "0660000006", "Fès", sdf.parse("18/06/1982"));
        Femme f7 = new Femme("Kabbaj", "Sara", "0660000007", "Marrakech", sdf.parse("25/04/1986"));
        Femme f8 = new Femme("Tahiri", "Imane", "0660000008", "Tanger", sdf.parse("30/08/1989"));
        Femme f9 = new Femme("Mansouri", "Fatima", "0660000009", "Agadir", sdf.parse("11/10/1960")); // Plus âgée
        Femme f10 = new Femme("Idrissi", "Khadija", "0660000010", "Meknès", sdf.parse("04/03/1992"));

        femmeService.create(f1);
        femmeService.create(f2);
        femmeService.create(f3);
        femmeService.create(f4);
        femmeService.create(f5);
        femmeService.create(f6);
        femmeService.create(f7);
        femmeService.create(f8);
        femmeService.create(f9);
        femmeService.create(f10);

        // 3. Mariages pour h1 (SAFI SAID) comme demandé dans l'exemple :
        // Mariages en cours : Salima Rami (1990, 4 enf), Amal Ali (1995, 2 enf), Wafa Alaoui (2000, 3 enf), Nadia Benjelloun (2005, 1 enf -> pour tester 4 femmes)
        // Mariage échoué : Karima Alami (1989 -> 1990, 0 enf)
        Mariage m1 = new Mariage(h1, f1, sdf.parse("03/09/1990"), null, 4);
        Mariage m2 = new Mariage(h1, f2, sdf.parse("03/09/1995"), null, 2);
        Mariage m3 = new Mariage(h1, f3, sdf.parse("04/11/2000"), null, 3);
        Mariage m4 = new Mariage(h1, f4, sdf.parse("03/09/1989"), sdf.parse("03/09/1990"), 0);
        Mariage m5 = new Mariage(h1, f5, sdf.parse("10/10/2005"), null, 1); // 4ème mariage actif
        mariageService.create(m1);
        mariageService.create(m2);
        mariageService.create(m3);
        mariageService.create(m4);
        mariageService.create(m5);

        // f4 se remarie avec h2 (pour tester les femmes mariées >= 2 fois)
        Mariage m6 = new Mariage(h2, f4, sdf.parse("15/06/1992"), null, 2);
        mariageService.create(m6);

        // Test 1 : Afficher la liste des femmes
        System.out.println("\n1. Liste des femmes enregistrées :");
        femmeService.findAll().forEach(f -> System.out.println("   * " + f.getNom() + " " + f.getPrenom() + " (Née le: " + sdf.format(f.getDateNaissance()) + ")"));

        // Test 2 : Femme la plus âgée
        Femme plusAgee = femmeService.afficherFemmePlusAgee();
        System.out.println("\n2. Femme la plus âgée : " + plusAgee.getNom() + " " + plusAgee.getPrenom() + " (Née le: " + sdf.format(plusAgee.getDateNaissance()) + ")");

        // Test 3 : Afficher les mariages de SAFI SAID (format exact)
        System.out.println("\n3. Affichage détaillé des mariages de Safi Said (format exact) :");
        hommeService.afficherMariagesHomme(h1);

        // Test 4 : Nombre d'enfants d'une femme entre deux dates (requête native nommée)
        int enfantsSalima = femmeService.compterEnfantsEntreDates(f1, sdf.parse("01/01/1980"), sdf.parse("31/12/2010"));
        System.out.println("\n4. Nombre d'enfants de Salima Rami (1980 - 2010) : " + enfantsSalima);

        // Test 5 : Femmes mariées au moins deux fois (requête nommée)
        System.out.println("\n5. Femmes mariées deux fois ou plus :");
        List<Femme> multMaries = femmeService.afficherFemmesMarieesAuMoinsDeuxFois();
        multMaries.forEach(f -> System.out.println("   * " + f.getNom() + " " + f.getPrenom()));

        // Test 6 : Hommes mariés à quatre femmes entre deux dates (Criteria API)
        long nbHommes4 = hommeService.compterHommesMariesQuatreFemmes(sdf.parse("01/01/1985"), sdf.parse("31/12/2010"));
        System.out.println("\n6. Nombre d'hommes mariés à au moins 4 femmes entre 1985 et 2010 : " + nbHommes4);
    }
}
