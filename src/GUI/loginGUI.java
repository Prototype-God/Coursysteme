package GUI;

import dao.AdministrateurDAO;

import dao.ConnectionDAO;
import dao.EtudiantDAO;

import model.Etudiants;

import javax.swing.*;

import org.jdesktop.swingx.JXPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import model.Admin;
import stockage.StudentStatut;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;


public class loginGUI {
	
	 private JFrame frame;
	    private JTextField usernameField;
	    private JPasswordField passwordField;

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            loginGUI login = new loginGUI();
	            login.getFrame().setVisible(true);
	        });
	    }

	    public loginGUI() {
	        initialize();
	    }

	  
	    private void initialize() {
	        setFrame(new JFrame("Connexion"));
	        getFrame().getContentPane().setBackground(new Color(221, 177, 226));
	        getFrame().getContentPane().setForeground(new Color(0, 0, 0));
	        getFrame().setBounds(100, 100, 450, 500);
	        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        getFrame().getContentPane().setLayout(null);
	        
	        // Charge l'image de fond
	        ImageIcon bgIcon = new ImageIcon(loginGUI.class.getResource("/pictures/EXT 14.jpg"));
	        BackgroundPanel backgroundPanel = new BackgroundPanel(bgIcon);
	        frame.setContentPane(backgroundPanel);

	        
	        JLabel userLabel = new JLabel("Username");
	        userLabel.setForeground(Color.RED);
	        userLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
	        userLabel.setBounds(50, 140, 120, 25);
	        frame.getContentPane().add(userLabel);
	        
	        
	        usernameField = new JTextField();
	        usernameField.setFont(new Font("Tahoma", Font.BOLD, 14));
	        usernameField.setBounds(50, 163, 342, 36);
	        getFrame().getContentPane().add(usernameField);

	        JLabel passwordLabel = new JLabel("Password");
	        passwordLabel.setForeground(Color.RED);
	        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
	        passwordLabel.setBounds(50, 223, 120, 25);
	        getFrame().getContentPane().add(passwordLabel);

	        passwordField = new JPasswordField();
	        passwordField.setFont(new Font("Times New Roman", Font.BOLD, 14));
	        passwordField.setBounds(50, 245, 342, 36);
	        getFrame().getContentPane().add(passwordField);

	        JButton adminLoginButton = new JButton("Admin Login");
	        adminLoginButton.setBackground(new Color(192, 192, 192));
	        adminLoginButton.setFont(new Font("Times New Roman", Font.BOLD, 12));
	        adminLoginButton.setBounds(20, 372, 120, 30);
	        getFrame().getContentPane().add(adminLoginButton);

	        JButton etudiantLoginButton = new JButton("Etudiant Login");
	        etudiantLoginButton.setBackground(new Color(192, 192, 192));
	        etudiantLoginButton.setFont(new Font("Times New Roman", Font.BOLD, 12));
	        etudiantLoginButton.setBounds(286, 372, 140, 30);
	        getFrame().getContentPane().add(etudiantLoginButton);
	        
	        JLabel lblNewLabel = new JLabel("WELCOME");
	        lblNewLabel.setForeground(Color.RED);
	        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
	        lblNewLabel.setBounds(129, 57, 231, 46);
	        getFrame().getContentPane().add(lblNewLabel);
	        
	        JLabel lblNewLabel_1 = new JLabel("New label");
	        lblNewLabel_1.setBounds(197, 257, 45, 13);
	        getFrame().getContentPane().add(lblNewLabel_1);
	        
	        JCheckBox chckbxNewCheckBox = new JCheckBox("Afficher le mot de passe");
	        chckbxNewCheckBox.setFont(new Font("Times New Roman", Font.BOLD, 12));
	        chckbxNewCheckBox.setBounds(50, 297, 172, 21);
	        
	        chckbxNewCheckBox.addActionListener(e -> {
	            if (chckbxNewCheckBox.isSelected()) {
	                passwordField.setEchoChar((char) 0); // Affiche le mot de passe
	            } else {
	                passwordField.setEchoChar('•'); // Cache le mot de passe
	            }
	        });
	        getFrame().getContentPane().add(chckbxNewCheckBox);

	        // 🔒 Bouton admin
	        adminLoginButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                String username = usernameField.getText();
	                String password = String.valueOf(passwordField.getPassword());

	                ConnectionDAO connectionDAO = new ConnectionDAO();
	                Connection conn = connectionDAO.getConnection();

	                if (conn == null) {
	                    JOptionPane.showMessageDialog(getFrame(), "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }

	                AdministrateurDAO adminDao = new AdministrateurDAO(conn);
	                Admin admin = adminDao.getByCredentials(username, password);

	                if (admin != null) {
	                	JOptionPane.showMessageDialog(getFrame(), "✅ Connexion admin réussie !\nBienvenue " + admin.getUsername());
	                	AdminIHM adihm = new AdminIHM();
	                    getFrame().dispose();
	                 
	                    // TODO: ouvrir l'interface AdminGUI
	                } else {
	                    JOptionPane.showMessageDialog(getFrame(), "❌ Identifiants admin incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        });

	        //  Bouton étudiant
	        etudiantLoginButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                String username = usernameField.getText();
	                StudentStatut.setIdStudent(username);
	                String password = String.valueOf(passwordField.getPassword());

	                ConnectionDAO connectionDAO = new ConnectionDAO();
	                Connection conn = connectionDAO.getConnection();

	                if (conn == null) {
	                    JOptionPane.showMessageDialog(getFrame(), "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }

	                EtudiantDAO etuDao = new EtudiantDAO(conn);
	                Etudiants etu = etuDao.getByCredentials(username, password);
                    
	                
	                if (etu != null) {
	                	JOptionPane.showMessageDialog(getFrame(),
	                		    " Connexion réussie !\nBienvenue " + etu.getPrenom() + " " + etu.getNom());
	                	
	                	int id = etu.getId();           // récupéré depuis la base de données
	                	String statut = etu.getStatut(); // FISE ou FISA

	                
	                	EtudiantIHM etuIHM = new EtudiantIHM();
	                	
	                    getFrame().dispose();
	                    
	                    // TODO: ouvrir StudentGUI	                
	                    } 
	                    else {
	                    JOptionPane.showMessageDialog(getFrame(), "❌ Identifiants étudiant incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        });
	    }

		public JFrame getFrame() {
			return frame;
		}

		public void setFrame(JFrame frame) {
			this.frame = frame;
		}
}


