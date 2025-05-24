package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DominanteDAO {

    public void ajouterDominante(String nom) throws SQLException {
        String sql = "INSERT INTO dominante (nom) VALUES (?)";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.executeUpdate();
        }
    }

    public void supprimerDominante(String nom) throws SQLException {
        String sql = "DELETE FROM dominante WHERE nom = ?";
        try (Connection conn = ConnectionDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.executeUpdate();
        }
    }

    public List<String> getToutesDominantes() throws SQLException {
        List<String> dominantes = new ArrayList<>();
        String sql = "SELECT nom FROM dominante";
        try (Connection conn = ConnectionDAO.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                dominantes.add(rs.getString("nom"));
            }
        }
        return dominantes;
    }
}
