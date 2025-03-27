package model;

import java.util.ArrayList;
import java.util.Collections;


public class FISE extends Etudiant {
    private int rang;
    private ArrayList<Dominante> listeDominantes;

    public FISE(String nom, String prenom, String filiere, int promo, String dateNaissance,
                String username, String password, int rang) {
        super(nom, prenom, filiere, promo, dateNaissance, username, password);
        this.rang = rang;
        this.listeDominantes = new ArrayList<>();
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }


    public ArrayList<Dominante> getListeDominantes() {
        return listeDominantes;
    }

    public boolean ajouterDominante(Dominante d) {
        if (listeDominantes.size() >= 5) return false;
        if (!d.aDesPlacesDisponibles()) return false;

        listeDominantes.add(d);
        d.addEtudiant(this); // Met à jour la dominante aussi
        return true;
    }

    public ArrayList<Dominante> classerLesDominantes() {
        Collections.sort(listeDominantes, (d1, d2) -> d1.getNom().compareToIgnoreCase(d2.getNom()));
        return listeDominantes;
    }
}