package GUI;

/*import dao.EtudiantDAO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;
import stockage.StudentStatut;

public class EtudiantIHM {

	 private JFrame jframe;
	 
	   public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            EtudiantIHM menu = new EtudiantIHM();
	            menu.getJframe().setVisible(true);
	        });
	    }  
	  
	 public EtudiantIHM() {
	      
	        initialize();  // Initialisation de l'interface
	    }

		  
		    private void initialize() {
		        setJframe(new JFrame("Menu Etudiant"));
		        getJframe().getContentPane().setFont(new Font("Times New Roman", Font.BOLD, 14));
		        getJframe().getContentPane().setBackground(new Color(175, 96, 255));
		        getJframe().getContentPane().setForeground(new Color(0, 0, 0));
		        getJframe().setBounds(100, 100, 600, 650);
		        getJframe().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        getJframe().getContentPane().setLayout(null);
		        
		        JButton btnNewButton = new JButton("FAIRE MES CHOIX");
		        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		        btnNewButton.setBounds(95, 211, 409, 75);
		        getJframe().getContentPane().add(btnNewButton);
		        
		       
		       
		        
		        JButton btnNewButton_1 = new JButton("VOIR MES INFOS");
		        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		        btnNewButton_1.addActionListener(new ActionListener() {
		        	
		        	public void actionPerformed(ActionEvent e) {
		        	}
		        });
		        btnNewButton_1.setBounds(95, 413, 409, 75);
		        getJframe().getContentPane().add(btnNewButton_1);
		        
		        JLabel lblNewLabel = new JLabel("MENU");
		        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 34));
		        lblNewLabel.setBounds(216, 81, 139, 51);
		        getJframe().getContentPane().add(lblNewLabel);
		        
		        JButton btnNewButton_2 = new JButton("Retour");
		        btnNewButton_2.addActionListener(new ActionListener() {
		        	
		       
		        	public void actionPerformed(ActionEvent e) {
		        		 getJframe().dispose();
		        		SwingUtilities.invokeLater(() -> {
		    	            loginGUI login = new loginGUI();
		    	            login.getFrame().setVisible(true);
		    	        });
		        	}
		        });
		        btnNewButton_2.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		        btnNewButton_2.setForeground(new Color(128, 0, 128));
		        btnNewButton_2.setBounds(10, 23, 85, 21);
		        getJframe().getContentPane().add(btnNewButton_2);
		        
		        btnNewButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
	
		            	//ouvre la fenêtre de choix selon le statut de l'étudiant 
		 		       
		 		       
				        String statut;
				        String identifiant;
				        identifiant = StudentStatut.getIdStudent();
				        statut = EtudiantDAO.getStudentStatus(identifiant);
				        
				        if ("FISE".equals(statut))
				        {
				       
				        	choix_FISE fise = new choix_FISE();
				        }
				        else if("FISA".equals(statut)) {
				        	
				        	choix_FISA fisa = new choix_FISA();
				        }
				        
				        else {
				            JOptionPane.showMessageDialog(getJframe(), "Statut inconnu : " + statut, "Erreur", JOptionPane.ERROR_MESSAGE);
				        }
		            }
		        });
		        
		        btnNewButton_1.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                new VoirInfo();
		                getJframe().setVisible(false); // cache la fenêtre mais elle reste en mémoire
		            }
		        });		        
		        
                getJframe().setVisible(true);
		        
		    }

			public JFrame getJframe() {
				return jframe;
			}

			public void setJframe(JFrame jframe) {
				this.jframe = jframe;
			}        
	    }*/


import com.formdev.flatlaf.FlatIntelliJLaf;
import dao.EtudiantDAO;
import stockage.StudentStatut;

import javax.swing.*;
import org.jdesktop.swingx.JXPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dao.ProcedureDAO;

public class EtudiantIHM {

    private JFrame jframe;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatIntelliJLaf.setup(); // Active un look moderne
            new EtudiantIHM();
        });
    }

    public EtudiantIHM() {
        initialize();
    }

    private void initialize() {
        jframe = new JFrame("🎓 Menu Étudiant");
        jframe.setSize(600, 650);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLocationRelativeTo(null); // Centrer la fenêtre

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 245, 255)); 

        // Titre
        JLabel titleLabel = new JLabel("MENU ÉTUDIANT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(70, 30, 150));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Centre la fenêtre avec les boutons
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(255, 176, 255));
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridLayout(3, 1, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JButton btnChoix = new JButton("🎯 FAIRE MES CHOIX");
        JButton btnInfos = new JButton("📋 VOIR MES INFOS");
        JButton btnRetour = new JButton("🔙 Retour");

        Font buttonFont = new Font("SansSerif", Font.BOLD, 18);
        btnChoix.setFont(buttonFont);
        btnInfos.setFont(buttonFont);
        btnRetour.setFont(new Font("SansSerif", Font.PLAIN, 14));

        centerPanel.add(btnChoix);
        centerPanel.add(btnInfos);
        centerPanel.add(btnRetour);

        panel.add(centerPanel, BorderLayout.CENTER);
        jframe.setContentPane(panel);

        // Actions
        btnChoix.addActionListener(e -> {
            String user = StudentStatut.getIdStudent();
            String statut = EtudiantDAO.getStudentStatus(user);

            if (!ProcedureDAO.isPeriodeOuverte(statut)) {
                JOptionPane.showMessageDialog(null, "⛔ La période de choix " + statut + " n'est pas ouverte !");
                return;
            }

            if (EtudiantDAO.AdejaChoisi(user)) {
                JOptionPane.showMessageDialog(jframe, "Vous avez déjà enregistré vos choix. Impossible d'en refaire.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if ("FISE".equals(statut)) {
                new choix_FISE();
            } else if ("FISA".equals(statut)) {
                new choix_FISA();
            } else {
                JOptionPane.showMessageDialog(jframe, "Statut inconnu : " + statut, "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnInfos.addActionListener(e -> {
            new VoirInfo();
            jframe.setVisible(false);
        });

        btnRetour.addActionListener(e -> {
            jframe.dispose();
            new loginGUI().getFrame().setVisible(true);
        });

        jframe.setVisible(true);
    }

    public JFrame getJframe() {
        return jframe;
    }

    public void setJframe(JFrame jframe) {
        this.jframe = jframe;
    }
}


