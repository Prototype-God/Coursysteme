package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe représentant un étudiant FISA
 */
public class FISA extends Etudiant {
    private ArrayList<Dominante> listeDominantes;

    public FISA(String nom, String prenom, String filiere, int promo, String dateNaissance,
                String username, String password, int rang, int idPh) {
        super(nom, prenom, filiere, promo, dateNaissance, username, password, rang, idPh);
        this.listeDominantes = new ArrayList<>();
    }

    public ArrayList<Dominante> getListeDominantes() {
        return listeDominantes;
    }

    /**
     * Ajoute une dominante si disponible et si < 5 choisies
     */
    public boolean ajouterDominante(Dominante d) {
        if (listeDominantes.size() >= 5) return false;
        if (!d.aDesPlacesDisponibles()) return false;

        listeDominantes.add(d);
        d.addEtudiant(this); 
        return true;
    }

    /**
     * Trie les dominantes par ordre alphabétique
     */
    public ArrayList<Dominante> classerLesDominantes() {
        Collections.sort(listeDominantes, (d1, d2) -> d1.getNom().compareToIgnoreCase(d2.getNom()));
        return listeDominantes;
    }
}