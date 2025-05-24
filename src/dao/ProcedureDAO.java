package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.Timestamp;


public class ProcedureDAO {

    public static boolean isFiseOuvert() {
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT FISE_OUVERT FROM PROCEDURE_STATUT WHERE ID = 1");
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("FISE_OUVERT") == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFisaOuvert() {
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT FISA_OUVERT FROM PROCEDURE_STATUT WHERE ID = 1");
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("FISA_OUVERT") == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setFiseOuvert(boolean ouvert) {
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE PROCEDURE_STATUT SET FISE_OUVERT = ? WHERE ID = 1")) {
            stmt.setInt(1, ouvert ? 1 : 0);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFisaOuvert(boolean ouvert) {
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE PROCEDURE_STATUT SET FISA_OUVERT = ? WHERE ID = 1")) {
            stmt.setInt(1, ouvert ? 1 : 0);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static boolean isFiseActuellementOuvert() {
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT FISE_DEBUT, FISE_FIN FROM PROCEDURE_STATUT WHERE ID = 1");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                LocalDate now = LocalDate.now();
                Date debut = rs.getDate("FISE_DEBUT");
                Date fin = rs.getDate("FISE_FIN");
                return !now.isBefore(debut.toLocalDate()) && !now.isAfter(fin.toLocalDate());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
   
    public static boolean isFisaActuellementOuvert() {
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT FISA_DEBUT, FISA_FIN FROM PROCEDURE_STATUT WHERE ID = 1");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                LocalDate now = LocalDate.now();
                Date debut = rs.getDate("FISA_DEBUT");
                Date fin = rs.getDate("FISA_FIN");
                return !now.isBefore(debut.toLocalDate()) && !now.isAfter(fin.toLocalDate());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean isPeriodeOuverte(String statut) {
        String query = "SELECT FISE_DEBUT, FISE_FIN, FISA_DEBUT, FISA_FIN FROM PROCEDURE_STATUT WHERE ID = 1";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Date today = new Date(System.currentTimeMillis());

                if ("FISE".equalsIgnoreCase(statut)) {
                    Date debut = rs.getDate("FISE_DEBUT");
                    Date fin = rs.getDate("FISE_FIN");
                    return today.compareTo(debut) >= 0 && today.compareTo(fin) <= 0;
                } else if ("FISA".equalsIgnoreCase(statut)) {
                    Date debut = rs.getDate("FISA_DEBUT");
                    Date fin = rs.getDate("FISA_FIN");
                    return today.compareTo(debut) >= 0 && today.compareTo(fin) <= 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void setFiseDates(java.util.Date debut, java.util.Date fin) {
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE PROCEDURE_STATUT SET FISE_DEBUT = ?, FISE_FIN = ? WHERE ID = 1")) {

            stmt.setTimestamp(1, new java.sql.Timestamp(debut.getTime()));
            stmt.setTimestamp(2, new java.sql.Timestamp(fin.getTime()));

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFisaDates(java.util.Date debut, java.util.Date fin) {
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE PROCEDURE_STATUT SET FISA_DEBUT = ?, FISA_FIN = ? WHERE ID = 1")) {

            stmt.setTimestamp(1, new java.sql.Timestamp(debut.getTime()));
            stmt.setTimestamp(2, new java.sql.Timestamp(fin.getTime()));

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


