package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;


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
        String sql = "INSERT INTO CHOICES (username, dominante, ordre_choix) " +
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

    
    
    
    public static void effectuerAttributionAutomatique(Connection conn) {
        try {
            // 获取所有未被关闭的 dominantes
            String sqlDominantes = "SELECT NOM FROM DOMINANTES WHERE ID_PH_D != 4";
            PreparedStatement psDom = conn.prepareStatement(sqlDominantes);
            ResultSet rsDominantes = psDom.executeQuery();

            while (rsDominantes.next()) {
                String nomDominante = rsDominantes.getString("NOM");

                for (int rangChoix = 1; rangChoix <= 5; rangChoix++) {
                    // 选了该 dominante、处于当前 rangChoix 且尚未被分配（ID_PH != 5）的学生
                    String sqlEtudiants = """
                        SELECT e.USERNAME, e.RANG
                        FROM SAVE_CHOIX_ETU sce
                        JOIN ETUDIANT e ON sce.USERNAME = e.USERNAME
                        WHERE sce.NOM_DOMIN = ?
                          AND sce.RANG_CHOIX = ?
                          AND (e.ID_PH IS NULL OR e.ID_PH != 5)
                        ORDER BY e.RANG ASC
                    """;

                    PreparedStatement psEtu = conn.prepareStatement(sqlEtudiants);
                    psEtu.setString(1, nomDominante);
                    psEtu.setInt(2, rangChoix);
                    ResultSet rsEtudiants = psEtu.executeQuery();

                    while (rsEtudiants.next()) {
                        String username = rsEtudiants.getString("USERNAME");

                        
                        PreparedStatement psDispo = conn.prepareStatement(
                            "SELECT PLACESDISPO FROM DOMINANTES WHERE NOM = ?");
                        psDispo.setString(1, nomDominante);
                        ResultSet rsDispo = psDispo.executeQuery();

                        if (rsDispo.next() && rsDispo.getInt("PLACESDISPO") > 0) {
                         
                            PreparedStatement psMajEtu = conn.prepareStatement(
                                "UPDATE ETUDIANT SET ID_PH = 5 WHERE USERNAME = ?");
                            psMajEtu.setString(1, username);
                            psMajEtu.executeUpdate();

                            PreparedStatement psMajDom = conn.prepareStatement(
                                "UPDATE DOMINANTES SET PLACESPRISES = PLACESPRISES + 1, PLACESDISPO = PLACESDISPO - 1 WHERE NOM = ?");
                            psMajDom.setString(1, nomDominante);
                            psMajDom.executeUpdate();

                            PreparedStatement psSuppr = conn.prepareStatement(
                                "DELETE FROM SAVE_CHOIX_ETU WHERE USERNAME = ? AND NOM_DOMIN != ?");
                            psSuppr.setString(1, username);
                            psSuppr.setString(2, nomDominante);
                            psSuppr.executeUpdate();

                         
                            PreparedStatement psCheckFull = conn.prepareStatement(
                                "SELECT PLACESDISPO FROM DOMINANTES WHERE NOM = ?");
                            psCheckFull.setString(1, nomDominante);
                            ResultSet rsCheck = psCheckFull.executeQuery();

                            if (rsCheck.next() && rsCheck.getInt("PLACESDISPO") <= 0) {
                                PreparedStatement psFermer = conn.prepareStatement(
                                    "UPDATE DOMINANTES SET ID_PH_D = 4 WHERE NOM = ?");
                                psFermer.setString(1, nomDominante);
                                psFermer.executeUpdate();
                                System.out.println("🔒 Dominante fermée : " + nomDominante);
                                break; 
                            }
                        }
                    }
                }

                System.out.println("✅ Traitement terminé pour : " + nomDominante);
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception ignored) {}
        }
    }

    public static void validerChoixParRang(Connection conn) throws SQLException {
        for (int rang = 1; rang <= 5; rang++) {
            String getChoices = """
                SELECT C.USERNAME, C.DOMINANTE
                FROM CHOICES C
                WHERE C.ORDRE_CHOIX = ? 
                  AND C.USERNAME NOT IN (
                      SELECT USERNAME FROM SAVE_CHOIX_ETU
                  )
            """;

            PreparedStatement stmt = conn.prepareStatement(getChoices);
            stmt.setInt(1, rang);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("USERNAME");
                String domin = rs.getString("DOMINANTE");

                
                String checkPlaces = "SELECT PLACESDISPO FROM DOMINANTES WHERE NOM = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkPlaces);
                checkStmt.setString(1, domin);
                ResultSet placesRS = checkStmt.executeQuery();

                if (placesRS.next() && placesRS.getInt("PLACESDISPO") > 0) {
                 
                    String insert = "INSERT INTO SAVE_CHOIX_ETU (USERNAME, NOM_DOMIN, RANG_CHOIX) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insert);
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, domin);
                    insertStmt.setInt(3, rang);
                    insertStmt.executeUpdate();

                    
                    String updateDom = "UPDATE DOMINANTES SET PLACESDISPO = PLACESDISPO - 1, PLACESPRISES = PLACESPRISES + 1 WHERE NOM = ?";
                    PreparedStatement updateDomStmt = conn.prepareStatement(updateDom);
                    updateDomStmt.setString(1, domin);
                    updateDomStmt.executeUpdate();

        
                    String deleteChoices = "DELETE FROM CHOICES WHERE USERNAME = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteChoices);
                    deleteStmt.setString(1, username);
                    deleteStmt.executeUpdate();

                    
                    String updateEtudiant = "UPDATE ETUDIANT SET ID_PH = 5 WHERE USERNAME = ?";
                    PreparedStatement updateEtuStmt = conn.prepareStatement(updateEtudiant);
                    updateEtuStmt.setString(1, username);
                    updateEtuStmt.executeUpdate();
                }
            }
        }
    }
    
    
    public List<String> getFiseDominantes(Connection conn) throws SQLException {
        List<String> dominantes = new ArrayList<>();
        String sql = "SELECT NOM FROM DOMINANTES WHERE ID_PH_D IN (1, 3)";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dominantes.add(rs.getString("NOM"));
            }
        }
        return dominantes;
    }
    public List<String> getFisaDominantes(Connection conn) throws SQLException {
        List<String> dominantes = new ArrayList<>();
        String sql = "SELECT NOM FROM DOMINANTES WHERE ID_PH_D IN (2, 4)";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dominantes.add(rs.getString("NOM"));
            }
        }
        return dominantes;
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
}  
