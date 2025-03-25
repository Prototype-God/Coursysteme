package model;

import java.util.ArrayList;

public class Dominante {
    private String nom;
    private int placesDispo;
    private int placesPrises;
    private ArrayList<Etudiant> listeEtudiants;

    public Dominante(String nom, int placesDispo, int placesPrises) {
        this.nom = nom;
        this.placesDispo = placesDispo;
        this.placesPrises = placesPrises;
        this.listeEtudiants = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPlacesDispo() {
        return placesDispo;
    }

    public void setPlacesDispo(int placesDispo) {
        this.placesDispo = placesDispo;
    }

    public int getPlacesPrises() {
        return placesPrises;
    }

    public void setPlacesPrises(int placesPrises) {
        this.placesPrises = placesPrises;
    }

    public ArrayList<Etudiant> getListeEtudiants() {
        return listeEtudiants;
    }

    /**
     * Vérifie s'il reste des places disponibles
     */
    public boolean aDesPlacesDisponibles() {
        return placesPrises < placesDispo;
    }

    /**
     * Ajoute un étudiant à cette dominante s'il reste de la place
     */
    public boolean addEtudiant(Etudiant e) {
        if (!aDesPlacesDisponibles()) return false;

        listeEtudiants.add(e);
        placesPrises++;
        return true;
    }

    @Override
    public String toString() {
        return nom + " (" + placesPrises + "/" + placesDispo + ")";
    }
}