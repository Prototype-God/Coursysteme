package gui;

import dao.AdministrateurDAO;
import dao.ConnectionDAO;
import dao.EtudiantDAO;
import model.Administrateur;
import model.Etudiant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class LoginGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginGUI login = new LoginGUI();
            login.frame.setVisible(true);
        });
    }

    public LoginGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Connexion - Choisir le type d'utilisateur");
        frame.setBounds(100, 100, 420, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel userLabel = new JLabel("Nom d'utilisateur:");
        userLabel.setBounds(50, 30, 120, 25);
        frame.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(180, 30, 150, 25);
        frame.add(usernameField);

        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setBounds(50, 70, 120, 25);
        frame.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(180, 70, 150, 25);
        frame.add(passwordField);

        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.setBounds(60, 130, 120, 30);
        frame.add(adminLoginButton);

        JButton etudiantLoginButton = new JButton("Etudiant Login");
        etudiantLoginButton.setBounds(220, 130, 140, 30);
        frame.add(etudiantLoginButton);

        // 🔒 Bouton admin
        adminLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                ConnectionDAO connectionDAO = new ConnectionDAO();
                Connection conn = connectionDAO.getConnection();

                if (conn == null) {
                    JOptionPane.showMessageDialog(frame, "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AdministrateurDAO adminDao = new AdministrateurDAO(conn);
                Administrateur admin = adminDao.getByCredentials(username, password);

                if (admin != null) {
                	JOptionPane.showMessageDialog(frame, "✅ Connexion admin réussie !\nBienvenue " + admin.getUsername());
                    frame.dispose();
                    // TODO: ouvrir l'interface AdminGUI
                } else {
                    JOptionPane.showMessageDialog(frame, "❌ Identifiants admin incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 🎓 Bouton étudiant
        etudiantLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                ConnectionDAO connectionDAO = new ConnectionDAO();
                Connection conn = connectionDAO.getConnection();

                if (conn == null) {
                    JOptionPane.showMessageDialog(frame, "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                EtudiantDAO etuDao = new EtudiantDAO(conn);
                Etudiant etu = etuDao.getByCredentials(username, password);

                if (etu != null) {
                	JOptionPane.showMessageDialog(frame,
                		    "✅ Connexion réussie !\nBienvenue " + etu.getPrenom() + " " + etu.getNom());
                    frame.dispose();
                    // TODO: ouvrir StudentGUI 选课页面
                } else {
                    JOptionPane.showMessageDialog(frame, "❌ Identifiants étudiant incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
