package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import model.Etudiants;
public class EtudiantDAO {

	 private Connection connection;

	    public EtudiantDAO(Connection connection) {
	        this.connection = connection;
	    }

	    /**
	     * Vérifie les identifiants et retourne un objet Etudiant avec rang et idPh
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
	    public Etudiants getByNomPrenom(String nom, String prenom) {

	        try {
	            String sql = "SELECT * FROM ETUDIANT WHERE UPPER(NOM) = ? AND UPPER(PRENOM) = ?";
	            PreparedStatement stmt = connection.prepareStatement(sql);
	            stmt.setString(1, nom.toUpperCase());
	            stmt.setString(2, prenom.toUpperCase());

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
	        try {
	            String sql = "SELECT NOM_DOMI, L_ORDRE FROM ETUDIANTS_DOMI WHERE USERNAME_ETUDIANT = ? ORDER BY L_ORDRE";
	            PreparedStatement stmt = connection.prepareStatement(sql);
	            stmt.setString(1, username);
	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                String nom = rs.getString("NOM_DOMI");
	                int ordre = rs.getInt("L_ORDRE");
	                dominantes.add(" - " + nom + " (Choix " + ordre + ")");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return dominantes;
	    }
}
