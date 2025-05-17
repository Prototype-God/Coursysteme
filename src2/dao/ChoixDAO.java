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

                        // 获取剩余名额
                        PreparedStatement psDispo = conn.prepareStatement(
                            "SELECT PLACESDISPO FROM DOMINANTES WHERE NOM = ?");
                        psDispo.setString(1, nomDominante);
                        ResultSet rsDispo = psDispo.executeQuery();

                        if (rsDispo.next() && rsDispo.getInt("PLACESDISPO") > 0) {
                            // 分配该 dominante：设置 ID_PH = 5
                            PreparedStatement psMajEtu = conn.prepareStatement(
                                "UPDATE ETUDIANT SET ID_PH = 5 WHERE USERNAME = ?");
                            psMajEtu.setString(1, username);
                            psMajEtu.executeUpdate();

                            // 更新名额
                            PreparedStatement psMajDom = conn.prepareStatement(
                                "UPDATE DOMINANTES SET PLACESPRISES = PLACESPRISES + 1, PLACESDISPO = PLACESDISPO - 1 WHERE NOM = ?");
                            psMajDom.setString(1, nomDominante);
                            psMajDom.executeUpdate();

                            // 删除该学生其余选择
                            PreparedStatement psSuppr = conn.prepareStatement(
                                "DELETE FROM SAVE_CHOIX_ETU WHERE USERNAME = ? AND NOM_DOMIN != ?");
                            psSuppr.setString(1, username);
                            psSuppr.setString(2, nomDominante);
                            psSuppr.executeUpdate();

                            // 如果分配完后没空位了，关闭该 dominante
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
                                break; // dominant 已满，停止处理当前 dominante 的学生
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

    
    public static void autoDistribuerSansChoix(Connection conn) {
        try {
            // 查找未分配的学生
            PreparedStatement psEtudiants = conn.prepareStatement(
                "SELECT USERNAME FROM ETUDIANT WHERE ID_PH IS NULL OR ID_PH != 5"
            );
            ResultSet rsEtudiants = psEtudiants.executeQuery();

            while (rsEtudiants.next()) {
                String username = rsEtudiants.getString("USERNAME");

                // 查找第一个还有空位的 dominante
                PreparedStatement psDominante = conn.prepareStatement(
                    "SELECT NOM FROM DOMINANTES WHERE PLACESDISPO > 0 AND ID_PH_D != 4 FETCH FIRST 1 ROWS ONLY"
                );
                ResultSet rsDom = psDominante.executeQuery();

                if (rsDom.next()) {
                    String dominante = rsDom.getString("NOM");

                    // 设置学生分配成功
                    PreparedStatement psAffect = conn.prepareStatement(
                        "UPDATE ETUDIANT SET ID_PH = 5 WHERE USERNAME = ?"
                    );
                    psAffect.setString(1, username);
                    psAffect.executeUpdate();

                    // 更新 dominante 的座位数
                    PreparedStatement psMajDom = conn.prepareStatement(
                        "UPDATE DOMINANTES SET PLACESPRISES = PLACESPRISES + 1, PLACESDISPO = PLACESDISPO - 1 WHERE NOM = ?"
                    );
                    psMajDom.setString(1, dominante);
                    psMajDom.executeUpdate();

                    // 添加一条 SAVE_CHOIX_ETU 记录
                    PreparedStatement psInsertChoix = conn.prepareStatement(
                        "INSERT INTO SAVE_CHOIX_ETU (USERNAME, NOM_DOMIN, RANG_CHOIX) VALUES (?, ?, 1)"
                    );
                    psInsertChoix.setString(1, username);
                    psInsertChoix.setString(2, dominante);
                    psInsertChoix.executeUpdate();
                    PreparedStatement psCheck = conn.prepareStatement(
                    	    "SELECT PLACESDISPO FROM DOMINANTES WHERE NOM = ?");
                    	psCheck.setString(1, dominante);
                    	ResultSet rsCheck = psCheck.executeQuery();

                    	if (rsCheck.next() && rsCheck.getInt("PLACESDISPO") <= 0) {
                    	    PreparedStatement psFermer = conn.prepareStatement(
                    	        "UPDATE DOMINANTES SET ID_PH_D = 4 WHERE NOM = ?");
                    	    psFermer.setString(1, dominante);
                    	    psFermer.executeUpdate();
                    	    System.out.println("🔒 Dominante fermée automatiquement : " + dominante);
                    	}
                    System.out.println("🟢 Étudiant " + username + " affecté à " + dominante);
                } else {
                    System.out.println("⚠️ Aucun dominante disponible pour " + username);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception ignored) {}
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
