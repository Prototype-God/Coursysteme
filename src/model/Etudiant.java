package model;


public class Etudiant {
	
	    protected String nom;
	    protected String prenom;
	    protected String filiere;
	    protected int promo;
	    protected String dateNaissance;
	    protected String username;
	    protected String password;
/**
 * Constructeur
 * @param nom 
 * @param prenom 
 * @param filiere
 * @param promo 
 * @param dateNaissance  
 * @param username 
 * @param password 
 * 
 */
	    public Etudiant(String nom, String prenom, String filiere, int promo, String dateNaissance, String username, String password) {
	        this.nom = nom;
	        this.prenom = prenom;
	        this.filiere = filiere;
	        this.promo = promo;
	        this.dateNaissance = dateNaissance;
	        this.username = username;
	        this.password = password;
	    }

	    public Etudiant(String nom2, String prenom2, String filiere2, int promo2, String dateNaissance2, String username2,
		String password2, int rang, int idPh) {
	// TODO Auto-generated constructor stub
}

		public String getNom() {
	        return nom;
	    }

	    public void setNom(String nom) {
	        this.nom = nom;
	    }

	    public String getPrenom() {
	        return prenom;
	    }

	    public void setPrenom(String prenom) {
	        this.prenom = prenom;
	    }

	    public String getFiliere() {
	        return filiere;
	    }

	    public void setFiliere(String filiere) {
	        this.filiere = filiere;
	    }

	    public int getPromo() {
	        return promo;
	    }

	    public void setPromo(int promo) {
	        this.promo = promo;
	    }

	    public String getDateNaissance() {
	        return dateNaissance;
	    }

	    public void setDateNaissance(String dateNaissance) {
	        this.dateNaissance = dateNaissance;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

}