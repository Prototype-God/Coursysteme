package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Etudiants;
public class EtudiantDAO {

	 private Connection connection;

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
		public List<String> getDominantesByUsername(String username) {
			List<String> dominantes = new ArrayList<>();
		    String sql = "SELECT NOM_DOMI FROM ETUDIANTS_DOMI WHERE USERNAME_ETUDIANT = ?";

		    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
		        stmt.setString(1, username);
		        ResultSet rs = stmt.executeQuery();

		        while (rs.next()) {
		            dominantes.add(rs.getString("NOM_DOMI"));
		        }
		    } catch (SQLException e) {
		        System.err.println("Erreur lors de la récupération des dominantes : " + e.getMessage());
		        // On retourne quand même une liste vide pour éviter NullPointerException
		    }
		    return dominantes;
		}
		
}
