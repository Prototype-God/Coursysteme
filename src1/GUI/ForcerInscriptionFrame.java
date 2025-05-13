package GUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import dao.ConnectionDAO;
import javax.swing.*;
import java.awt.*;

public class ForcerInscriptionFrame {

    private JFrame frame;

    public ForcerInscriptionFrame() {
        frame = new JFrame("Forcer l'inscription");
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === TITRE ===
        JLabel title = new JLabel("Forcer l'inscription d'un étudiant");
        title.setFont(new Font("Tahoma", Font.BOLD, 16));
        title.setBounds(100, 20, 400, 30);
        frame.add(title);

        // === NOM / PRENOM ===
        JLabel nomLabel = new JLabel("Nom :");
        nomLabel.setBounds(50, 70, 100, 25);
        frame.add(nomLabel);

        JTextField nomField = new JTextField();
        nomField.setBounds(150, 70, 250, 25);
        frame.add(nomField);

        JLabel prenomLabel = new JLabel("Prénom :");
        prenomLabel.setBounds(50, 110, 100, 25);
        frame.add(prenomLabel);

        JTextField prenomField = new JTextField();
        prenomField.setBounds(150, 110, 250, 25);
        frame.add(prenomField);

        // === Dominante dropdown ===
        JLabel domLabel = new JLabel("Dominante :");
        domLabel.setBounds(50, 150, 100, 25);
        frame.add(domLabel);

        JComboBox<String> dominanteCombo = new JComboBox<>();
        dominanteCombo.setBounds(150, 150, 250, 25);
        try (Connection conn = ConnectionDAO.getConnection();
        	     Statement stmt = conn.createStatement();
        	     ResultSet rs = stmt.executeQuery("SELECT NOM FROM DOMINANTES")) {

        	    while (rs.next()) {
        	        dominanteCombo.addItem(rs.getString("NOM"));
        	    }

        	} catch (Exception e) {
        	    e.printStackTrace();
        	    JOptionPane.showMessageDialog(frame, "⚠️ Erreur lors du chargement des dominantes.");
        	}
        frame.add(dominanteCombo);

        JLabel dispoLabel = new JLabel("Modifier les places ?");
        dispoLabel.setBounds(50, 190, 150, 25);
        frame.add(dispoLabel);

        JCheckBox dispoCheckBox = new JCheckBox("Oui");
        dispoCheckBox.setBounds(210, 190, 60, 25);
        frame.add(dispoCheckBox);

        // === Valider bouton ===
        JButton validerBtn = new JButton("Valider");
        validerBtn.setBounds(180, 240, 120, 35);
        frame.add(validerBtn);
        validerBtn.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String dominante = (String) dominanteCombo.getSelectedItem();

            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "❗Veuillez remplir le nom et le prénom.");
                return;
            }

            try (Connection conn = ConnectionDAO.getConnection()) {
            
                PreparedStatement psUser = conn.prepareStatement(
                    "SELECT USERNAME FROM ETUDIANT WHERE UPPER(NOM) = UPPER(?) AND UPPER(PRENOM) = UPPER(?)");
                psUser.setString(1, nom);
                psUser.setString(2, prenom);
                ResultSet rsUser = psUser.executeQuery();

                if (!rsUser.next()) {
                    JOptionPane.showMessageDialog(frame, "⚠️ Étudiant non trouvé.");
                    return;
                }

                String username = rsUser.getString("USERNAME");

                conn.setAutoCommit(false); 

               
                PreparedStatement psUpdateStudent = conn.prepareStatement(
                    "UPDATE ETUDIANT SET ID_PH = 5 WHERE USERNAME = ?");
                psUpdateStudent.setString(1, username);
                psUpdateStudent.executeUpdate();

                PreparedStatement psUpdateDom = conn.prepareStatement(
                    "UPDATE DOMINANTES SET PLACESDISPO = PLACESDISPO - 1, PLACESPRISES = PLACESPRISES + 1 WHERE NOM = ?");
                psUpdateDom.setString(1, dominante);
                psUpdateDom.executeUpdate();

            
                PreparedStatement psInsertChoix = conn.prepareStatement(
                    "INSERT INTO SAVE_CHOIX_ETU (USERNAME, NOM_DOMIN, RANG_CHOIX) VALUES (?, ?, 1)");
                psInsertChoix.setString(1, username);
                psInsertChoix.setString(2, dominante);
                psInsertChoix.executeUpdate();

                conn.commit();
                JOptionPane.showMessageDialog(frame, "✅ Étudiant forcé avec succès à : " + dominante);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "❌ Erreur lors de l'attribution forcée.");
            }
        });
        
        
        
        dispoCheckBox.addActionListener(e -> {
            if (dispoCheckBox.isSelected()) {
                String selectedDominante = (String) dominanteCombo.getSelectedItem();

                try (Connection conn = ConnectionDAO.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                             "UPDATE DOMINANTES SET PLACESDISPO = PLACESDISPO + 1 WHERE NOM = ?")) {

                    stmt.setString(1, selectedDominante);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(frame,
                        "✅ 1 place ajoutée à la dominante : " + selectedDominante);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                        "❌ Erreur lors de la mise à jour des places.");
                    dispoCheckBox.setSelected(false);
                }
            }
        });
        frame.setVisible(true);
    }
}