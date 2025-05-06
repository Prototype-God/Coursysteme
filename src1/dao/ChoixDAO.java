package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ChoixDAO {

    /**
     * Enregistre les choix d’un étudiant dans la BDD.
     * @param username Le nom d'utilisateur de l'étudiant
     * @param nom Nom de l'étudiant
     * @param prenom Prénom de l'étudiant
     * @param statut Statut (FISE ou FISA)
     * @param choixDominantes Liste ordonnée des dominantes choisies
     */
    public static void validerChoix(String username, List<String> choixDominantes) {
        String sql = "INSERT INTO SAVE_CHOIX_ETU (username,nom_domin, rang_choix) " +
                     "VALUES (?, ?, ?)";

        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < choixDominantes.size(); i++) {
                stmt.setString(1, username);
                
                stmt.setString(2, choixDominantes.get(i));
                stmt.setInt(3, i + 1); // rang_choix

                stmt.addBatch();
            }

            stmt.executeBatch();//exécuté tous les choix faits en une fois
            System.out.println(" Les choix de " + username + " ont été enregistrés " );

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }

   /**
     * Supprime les anciens choix d’un étudiant.
     * @param username Le nom d'utilisateur de l'étudiant
     */
    /* public static void supprimerChoixEtudiant(String username) {
        String sql = "DELETE FROM choix_etudiants WHERE username = ?";

        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur de suppression : " + e.getMessage());
        }
    }*/
}