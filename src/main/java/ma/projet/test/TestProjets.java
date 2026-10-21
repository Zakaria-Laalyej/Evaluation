package ma.projet.test;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestProjets {

    public static void executer() throws Exception {
        System.out.println("\n=================================================================");
        System.out.println("   EXERCICE 2 : APPLICATION DE GESTION DE PROJETS");
        System.out.println("=================================================================");

        EmployeService empService = new EmployeService();
        ProjetService projService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService etService = new EmployeTacheService();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // 1. Employé responsable
        Employe ingenieur = new Employe("Alaoui", "Ahmed", "0612355878");
        empService.create(ingenieur);

        // 2. Projet
        Projet proj4 = new Projet("Gestion de stock", sdf.parse("15/01/2013"), sdf.parse("30/06/2013"), ingenieur);
        projService.create(proj4);

        // 3. Tâches
        Tache t12 = new Tache("Analyse", sdf.parse("01/02/2013"), sdf.parse("28/02/2013"), 3500.0, proj4);
        Tache t13 = new Tache("Conception", sdf.parse("01/03/2013"), sdf.parse("31/03/2013"), 2500.0, proj4);
        Tache t14 = new Tache("Développement", sdf.parse("01/04/2013"), sdf.parse("30/04/2013"), 5000.0, proj4);
        tacheService.create(t12);
        tacheService.create(t13);
        tacheService.create(t14);

        // 4. Assignation avec dates réelles
        EmployeTache et1 = new EmployeTache(ingenieur, t12, sdf.parse("10/02/2013"), sdf.parse("20/02/2013"));
        EmployeTache et2 = new EmployeTache(ingenieur, t13, sdf.parse("10/03/2013"), sdf.parse("15/03/2013"));
        EmployeTache et3 = new EmployeTache(ingenieur, t14, sdf.parse("10/04/2013"), sdf.parse("25/04/2013"));
        etService.create(et1);
        etService.create(et2);
        etService.create(et3);

        // Affichage des tâches réalisées (format exact)
        System.out.println("\n-> Affichage des tâches réalisées pour le Projet 4 (format exact) :");
        projService.afficherTachesRealisees(proj4);

        // Tâches dont le prix > 1000 DH (requête nommée)
        System.out.println("\n-> Tâches dont le prix > 1000 DH (Requête Nommée) :");
        List<Tache> tachesCheres = tacheService.afficherTachesPrixSuperieur1000();
        tachesCheres.forEach(t -> System.out.println("   * " + t.getNom() + " -> " + t.getPrix() + " DH"));
    }
}
