package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Etudiants;
import model.FISA;
import model.FISE;
import stockage.StudentStatut;
import model.Dominantes;
import java.util.List;

import GUI.choix_FISA;
import GUI.choix_FISE;

import java.util.ArrayList;

public class EtudiantDAO {

	 private Connection connection;
	 
	 public EtudiantDAO ()
     {
    	 
     }

	    public EtudiantDAO(Connection connection) {
	        this.connection = connection;
	    }

	    /**
	     * Vérifie les identifiants et retourne un objet Etudiant avec son nom et prénom
	     */
	    public Etudiants getByCredentials(String username, String password) {
	        try {
	            System.out.println("Tentative de connexion étudiant : " + username + " / " + password);

	            String sql = "SELECT * FROM ETUDIANT WHERE UPPER(USERNAME) = ? AND PASSWORD = ?";
	            PreparedStatement stmt = connection.prepareStatement(sql);
	            stmt.setString(1, username.toUpperCase());
	            stmt.setString(2, password);

	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                String nom = rs.getString("NOM");
	                String prenom = rs.getString("PRENOM");
	                String filiere = rs.getString("FILIERE");
	                int promo = rs.getInt("PROMO");
	                String dateNaissance = rs.getString("DATENAISSANCE");
	                
	                return new Etudiants(nom, prenom, filiere, promo, dateNaissance, username, password);
	                
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	          return null; // Aucun étudiant trouvé
	    }
	    // récupération du statut FISE ou FISA de l'étudiant
	    
	    public static String getStudentStatus(String user) {
	        String status = "";
	        String query = "SELECT filiere FROM etudiant WHERE username = ?";
	        
	        try (Connection conn = ConnectionDAO.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            
	            stmt.setString(1, user);
	            ResultSet rs = stmt.executeQuery();
	            
	            if (rs.next()) {
	                status = rs.getString("filiere");
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	        return status;
	    }
	    
	    public List<String> getDominantesByUsername(String username) {
	        List<String> dominantes = new ArrayList<>();
	        String sql = "SELECT NOM_DOMIN FROM SAVE_CHOIX_ETU WHERE USERNAME = ? ORDER BY RANG_CHOIX";

	        try (Connection conn = ConnectionDAO.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setString(1, username);
	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                dominantes.add(rs.getString("NOM_DOMIN"));
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return dominantes;
	    }
	    
	    public static List<String> getAllDominantes() {
	        List<String> dominantes = new ArrayList<>();

	        try (Connection conn = ConnectionDAO.getConnection();
	             PreparedStatement stmt = conn.prepareStatement("SELECT NOM FROM DOMINANTES");
	             ResultSet rs = stmt.executeQuery()) {

	            while (rs.next()) {
	                dominantes.add(rs.getString("NOM"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return dominantes;
	    }
	    
	    public static Etudiants getStudentInfos(String username) {
	        Etudiants etu = null;

	        try (Connection conn = ConnectionDAO.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(
	                     "SELECT * FROM etudiant WHERE username = ?")) {

	            stmt.setString(1, username);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                String statut = rs.getString("filiere");

	                if ("FISE".equalsIgnoreCase(statut)) {
	                    FISE fise = new FISE();
	                    fise.setNom(rs.getString("nom"));
	                    fise.setPrenom(rs.getString("prenom"));
	                    fise.setPromo(rs.getInt("promo"));
	                    fise.setStatut(statut);
	                    fise.setRang(rs.getInt("rang"));
	                    fise.setDateNaissance(rs.getString("datenaissance"));
	                    etu = fise;

	                } else if ("FISA".equalsIgnoreCase(statut)) {
	                    FISA fisa = new FISA();
	                    fisa.setNom(rs.getString("nom"));
	                    fisa.setPrenom(rs.getString("prenom"));
	                    fisa.setPromo(rs.getInt("promo"));
	                    fisa.setStatut(statut);
	                    fisa.setRang(rs.getInt("rang"));
	                    fisa.setDateNaissance(rs.getString("datenaissance"));
	                    etu = fisa;
	                }
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return etu;
	    }
	   
	    
	    public static List<Dominantes> getAllDominantesFull() {
	        List<Dominantes> dominantes = new ArrayList<>();

	        try (Connection conn = ConnectionDAO.getConnection();
	             PreparedStatement stmt = conn.prepareStatement("SELECT nom, placesdispo, placesprises FROM dominantes");
	             ResultSet rs = stmt.executeQuery()) {

	            while (rs.next()) {
	                Dominantes d = new Dominantes();
	                d.setNom(rs.getString("nom"));
	                d.setPlacesDispo(rs.getInt("placesdispo"));
	                d.setPlacesPrises(rs.getInt("placesprises"));
	                dominantes.add(d);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return dominantes;
	    }
	    
	    public Etudiants getByNomPrenom(String nom, String prenom) {

		    try {
		        String sql = "SELECT * FROM ETUDIANT WHERE NOM = ? AND PRENOM = ?";
		        PreparedStatement stmt = connection.prepareStatement(sql);
		        stmt.setString(1, nom);
		        stmt.setString(2, prenom);
		        ResultSet rs = stmt.executeQuery();

		        if (rs.next()) {
		            return new Etudiants(
		                rs.getString("NOM"),
		                rs.getString("PRENOM"),
		                rs.getString("FILIERE"),
		                rs.getInt("PROMO"),
		                rs.getString("DATENAISSANCE"),
		                rs.getString("USERNAME"),
		                rs.getString("PASSWORD")
		            );
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null;
		}
 
	    public static boolean AdejaChoisi(String username) {
	        
	    	String sql = "SELECT COUNT(*) FROM choices WHERE username = ?";

	        try (Connection con = ConnectionDAO.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, username);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                int count = rs.getInt(1);
	                return count > 0; // vrai s'il y a au moins 1 choix enregistré
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false; // par défaut, pas de choix enregistrés
	    } 
	    
}