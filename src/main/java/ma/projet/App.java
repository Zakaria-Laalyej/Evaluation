package ma.projet;

import ma.projet.test.TestStock;
import ma.projet.test.TestProjets;
import ma.projet.test.TestEtatCivil;
import ma.projet.util.HibernateUtil;

public class App {

    public static void main(String[] args) {
        System.out.println("#####################################################################");
        System.out.println("#         ÉVALUATION PRATIQUE HIBERNATE & JPA                #");
        System.out.println("#####################################################################\n");

        try {
            TestStock.executer();
            TestProjets.executer();
            TestEtatCivil.executer();

            System.out.println("\n>>> TOUS LES EXERCICES DE L'ÉVALUATION ONT ÉTÉ VALIDÉS AVEC SUCCÈS ! <<<");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
