package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    
    
    public static void autoDistribuerSansChoix(Connection conn) {
        try {
            // chercher l'etudiant（ID_PH ≠ 5）
            PreparedStatement psEtudiants = conn.prepareStatement(
                "SELECT USERNAME, FILIERE FROM ETUDIANT WHERE ID_PH IS NULL OR ID_PH != 5"
            );
            ResultSet rsEtudiants = psEtudiants.executeQuery();

            while (rsEtudiants.next()) {
                String username = rsEtudiants.getString("USERNAME");
                String filiere = rsEtudiants.getString("FILIERE");

                // d'apres FISE et FISA
                String sql = "SELECT NOM FROM DOMINANTES " +
                             "WHERE PLACESDISPO > 0 AND ID_PH_D != 4 AND (ID_PH_D = 3 OR ID_PH_D = ?) " +
                             "FETCH FIRST 1 ROWS ONLY";

                int filiereCode = filiere.equalsIgnoreCase("FISE") ? 1 : 2;

                PreparedStatement psDominante = conn.prepareStatement(sql);
                psDominante.setInt(1, filiereCode);
                ResultSet rsDom = psDominante.executeQuery();

                if (rsDom.next()) {
                    String dominante = rsDom.getString("NOM");

                    // renouvelle ID_PH
                    PreparedStatement psUpdateEtu = conn.prepareStatement(
                        "UPDATE ETUDIANT SET ID_PH = 5 WHERE USERNAME = ?");
                    psUpdateEtu.setString(1, username);
                    psUpdateEtu.executeUpdate();

                    // Renouvelle la place de dominant
                    PreparedStatement psUpdateDom = conn.prepareStatement(
                        "UPDATE DOMINANTES SET PLACESDISPO = PLACESDISPO - 1, PLACESPRISES = PLACESPRISES + 1 WHERE NOM = ?");
                    psUpdateDom.setString(1, dominante);
                    psUpdateDom.executeUpdate();

                    // ajouter dans  SAVE_CHOIX_ETU
                    PreparedStatement psCheck = conn.prepareStatement(
                        "SELECT COUNT(*) FROM SAVE_CHOIX_ETU WHERE USERNAME = ? AND NOM_DOMIN = ?");
                    psCheck.setString(1, username);
                    psCheck.setString(2, dominante);
                    ResultSet rsCheck = psCheck.executeQuery();
                    rsCheck.next();
                    if (rsCheck.getInt(1) == 0) {
                        PreparedStatement psInsert = conn.prepareStatement(
                            "INSERT INTO SAVE_CHOIX_ETU (USERNAME, NOM_DOMIN, RANG_CHOIX) VALUES (?, ?, 1)");
                        psInsert.setString(1, username);
                        psInsert.setString(2, dominante);
                        psInsert.executeUpdate();
                    }

                    // Si il n'a pas de la place  0，fermer dominante
                    PreparedStatement psFinalCheck = conn.prepareStatement(
                        "SELECT PLACESDISPO FROM DOMINANTES WHERE NOM = ?");
                    psFinalCheck.setString(1, dominante);
                    ResultSet rsFinal = psFinalCheck.executeQuery();
                    if (rsFinal.next() && rsFinal.getInt(1) <= 0) {
                        PreparedStatement psFermer = conn.prepareStatement(
                            "UPDATE DOMINANTES SET ID_PH_D = 4 WHERE NOM = ?");
                        psFermer.setString(1, dominante);
                        psFermer.executeUpdate();
                    }

                    System.out.println("🟢 " + username + " → " + dominante);
                } else {
                    System.out.println("⚠️ Aucun dominante disponible pour " + username + " (" + filiere + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception ignore) {}
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
