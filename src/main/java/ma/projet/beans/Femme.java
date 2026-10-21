package ma.projet.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "femmes")
@NamedQueries({
    @NamedQuery(name = "Femme.findMarriedAtLeastTwice",
                query = "SELECT m.femme FROM Mariage m GROUP BY m.femme HAVING COUNT(m) >= 2")
})
@NamedNativeQueries({
    @NamedNativeQuery(name = "Femme.countChildrenBetweenDates",
                      query = "SELECT COALESCE(SUM(m.nbrEnfants), 0) FROM mariages m WHERE m.femme_id = :femmeId AND m.dateDebut BETWEEN :d1 AND :d2")
})
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mariage> mariages = new ArrayList<>();

    public Femme() {}

    public Femme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() { return mariages; }
    public void setMariages(List<Mariage> mariages) { this.mariages = mariages; }
}
