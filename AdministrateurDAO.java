package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Administrateur;

public class AdministrateurDAO {

    private Connection connection;

    public AdministrateurDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Vérifie si un administrateur avec ce login existe
     */
    public Administrateur getByCredentials(String username, String password) {
        try {
            String sql = "SELECT * FROM ADMINISTRATEUR WHERE USERNAME = ? AND PASSWORD = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Login réussi
                return new Administrateur(rs.getString("USERNAME"), rs.getString("PASSWORD"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Aucun administrateur trouvé
    }
}