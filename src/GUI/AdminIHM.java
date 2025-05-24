package GUI;


import dao.ConnectionDAO;
import dao.EtudiantDAO;
import model.Etudiants;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.awt.Font;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class AdminIHM {

    private JFrame jframe;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminIHM menu1 = new AdminIHM();
            menu1.jframe.setVisible(true);
        });
    }

    public AdminIHM() {
        initialize();
    }

    
    
    private void initialize() {
        jframe = new JFrame("Menu Adminisrateur");
        jframe.getContentPane().setFont(new Font("Times New Roman", Font.BOLD, 14));
        jframe.getContentPane().setBackground(new Color(175, 96, 255));
        jframe.getContentPane().setForeground(new Color(0, 0, 0));
        jframe.setBounds(100, 100, 600, 850);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("MENU");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 38));
        lblNewLabel.setBounds(195, 0, 167, 92);
        jframe.getContentPane().add(lblNewLabel);

        
        
        // premier ponction
        JButton btnNewButton = new JButton("SUIVI DES CHOIX");
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnNewButton.setBounds(125, 102, 313, 56);
        jframe.getContentPane().add(btnNewButton);

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame saisieFrame = new JFrame("Rechercher un étudiant");
                saisieFrame.setSize(400, 300);
                saisieFrame.getContentPane().setLayout(null);

                JLabel nomLabel = new JLabel("Nom:");
                nomLabel.setBounds(50, 50, 100, 25);
                JTextField nomField = new JTextField();
                nomField.setBounds(150, 50, 180, 25);

                JLabel prenomLabel = new JLabel("Prénom:");
                prenomLabel.setBounds(50, 100, 100, 25);
                JTextField prenomField = new JTextField();
                prenomField.setBounds(150, 100, 180, 25);

                JButton searchBtn = new JButton("Rechercher");
                searchBtn.setBounds(130, 160, 120, 30);

                saisieFrame.getContentPane().add(nomLabel);
                saisieFrame.getContentPane().add(nomField);
                saisieFrame.getContentPane().add(prenomLabel);
                saisieFrame.getContentPane().add(prenomField);
                saisieFrame.getContentPane().add(searchBtn);

                saisieFrame.setVisible(true);

                searchBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String nom = nomField.getText().trim();
                        String prenom = prenomField.getText().trim();

                        ConnectionDAO cdao = new ConnectionDAO();
                        Connection conn = cdao.getConnection();
                        EtudiantDAO edao = new EtudiantDAO(conn);
                        Etudiants etu = edao.getByNomPrenom(nom, prenom);

                        if (etu != null) {
                            List<String> dominantes = edao.getDominantesByUsername(etu.getUsername());
                            StringBuilder domiText = new StringBuilder();
                            if (dominantes.isEmpty()) {
                                domiText.append("Aucun choix enregistré.");
                            } else {
                                domiText.append("Dominantes choisies :\n");
                                for (String choix : dominantes) {
                                    domiText.append(" - ").append(choix).append("\n");
                                }
                            }

                            JOptionPane.showMessageDialog(saisieFrame,
                                    "Nom: " + etu.getNom() + "\n" +
                                    "Prénom: " + etu.getPrenom() + "\n" +
                                    "Filière: " + etu.getStatut() + "\n" +
                                    "Promo: " + etu.getPromo() + "\n" +
                                    "Date Naissance: " + etu.getDateNaissance() + "\n" +
                                    "Username: " + etu.getUsername() + "\n" +
                                    "Password: " + etu.getPassword() + "\n\n" +
                                    domiText.toString()
                            );
                        } else {
                            JOptionPane.showMessageDialog(saisieFrame, "❌ Étudiant non trouvé.");
                        }
                    }
                });
            }
        });

        JButton btnNewButton_1 = new JButton("Modifier l'ETAT de dominant");
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnNewButton_1.setBounds(125, 191, 313, 56);
        jframe.getContentPane().add(btnNewButton_1);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ProcedureFrame(); 
            }
        });

        JButton btnNewButton_2 = new JButton("MODIFIER LES INFORMATIONS");
        btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnNewButton_2.setBounds(127, 284, 311, 56);
        jframe.getContentPane().add(btnNewButton_2);


        btnNewButton_2 .addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame gestionFrame = new JFrame("SAISIR DES INFORMATIONS");
                gestionFrame.setSize(400, 300);
                gestionFrame.getContentPane().setLayout(null);

                JButton btnAddStudent = new JButton("Ajouter étudiant");
                btnAddStudent.setBounds(100, 30, 200, 30);
                btnAddStudent.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame addFrame = new JFrame("Ajouter un étudiant");
                        addFrame.setSize(400, 400);
                        addFrame.getContentPane().setLayout(null);

                        String[] labels = { "Username", "Nom", "Prénom", "Filière", "Promo", "Date naissance (YYYY-MM-DD)", "Password", "Rang" };
                        JTextField[] fields = new JTextField[labels.length];

                        for (int i = 0; i < labels.length; i++) {
                            JLabel label = new JLabel(labels[i] + ":");
                            label.setBounds(30, 30 + i * 40, 180, 25);
                            addFrame.getContentPane().add(label);

                            fields[i] = new JTextField();
                            fields[i].setBounds(200, 30 + i * 40, 150, 25);
                            addFrame.getContentPane().add(fields[i]);
                        }

                        JButton confirmBtn = new JButton("Ajouter");
                        confirmBtn.setBounds(130, 340, 120, 30);
                        addFrame.getContentPane().add(confirmBtn);

                        confirmBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String username = fields[0].getText().trim();
                                String nom = fields[1].getText().trim();
                                String prenom = fields[2].getText().trim();
                                String filiere = fields[3].getText().trim();
                                String promoStr = fields[4].getText().trim();
                                String dateNaiss = fields[5].getText().trim();
                                String password = fields[6].getText().trim();
                                String rangStr = fields[7].getText().trim();

                                try {
                                    int promo = Integer.parseInt(promoStr);
                                    int rang = Integer.parseInt(rangStr);
                                    int idPh = 6; // 固定 ID_PH

                                    Connection conn = ConnectionDAO.getConnection();

                                    String sql = "INSERT INTO ETUDIANT " +
                                                 "(USERNAME, NOM, PRENOM, FILIERE, PROMO, DATENAISSANCE, PASSWORD, RANG, ID_PH) " +
                                                 "VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";
                                    PreparedStatement stmt = conn.prepareStatement(sql);
                                    stmt.setString(1, username);
                                    stmt.setString(2, nom);
                                    stmt.setString(3, prenom);
                                    stmt.setString(4, filiere);
                                    stmt.setInt(5, promo);
                                    stmt.setString(6, dateNaiss);
                                    stmt.setString(7, password);
                                    stmt.setInt(8, rang);
                                    stmt.setInt(9, idPh); // 写死为 6

                                    stmt.executeUpdate();
                                    JOptionPane.showMessageDialog(addFrame, "✅ Étudiant ajouté avec succès !");
                                    addFrame.dispose();

                                } catch (NumberFormatException nfe) {
                                    JOptionPane.showMessageDialog(addFrame, "❌ Promo ou Rang doit être un nombre.");
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(addFrame, "❌ Erreur: " + ex.getMessage());
                                }
                            }
                        });

                        addFrame.setVisible(true);
                    }
                });
                
                JButton btnDeleteStudent = new JButton("Supprimer étudiant");
                btnDeleteStudent.setBounds(100, 70, 200, 30);
                btnDeleteStudent.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame delFrame = new JFrame("Supprimer un étudiant");
                        delFrame.setSize(350, 180);
                        delFrame.getContentPane().setLayout(null);

                        JLabel userLabel = new JLabel("Username de l'étudiant à supprimer :");
                        userLabel.setBounds(20, 20, 300, 25);
                        delFrame.getContentPane().add(userLabel);

                        JTextField userField = new JTextField();
                        userField.setBounds(20, 50, 290, 25);
                        delFrame.getContentPane().add(userField);

                        JButton deleteBtn = new JButton("Supprimer");
                        deleteBtn.setBounds(100, 90, 120, 30);
                        delFrame.getContentPane().add(deleteBtn);

                        deleteBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String username = userField.getText().trim();

                                try {
                                    ConnectionDAO cdao = new ConnectionDAO();
                                    Connection conn = cdao.getConnection();
                                    Statement stmt = conn.createStatement();
                                    String sql = "DELETE FROM ETUDIANT WHERE USERNAME = '" + username + "'";
                                    int rows = stmt.executeUpdate(sql);

                                    if (rows > 0) {
                                        JOptionPane.showMessageDialog(delFrame, "✅ Étudiant supprimé avec succès !");
                                        delFrame.dispose();
                                    } else {
                                        JOptionPane.showMessageDialog(delFrame, "❌ Aucun étudiant trouvé avec ce username.");
                                    }
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(delFrame, "❌ Erreur: " + ex.getMessage());
                                }
                            }
                        });

                        delFrame.setVisible(true);
                    }
                });
                JButton btnAddDominante = new JButton("Ajouter dominante");
                btnAddDominante.setBounds(100, 110, 200, 30);
                btnAddDominante.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame addDomFrame = new JFrame("Ajouter une dominante");
                        addDomFrame.setSize(400, 300);
                        addDomFrame.getContentPane().setLayout(null);

                        JLabel nomLabel = new JLabel("Nom dominante:");
                        nomLabel.setBounds(30, 30, 150, 25);
                        JTextField nomField = new JTextField();
                        nomField.setBounds(180, 30, 150, 25);

                        JLabel placesLabel = new JLabel("Places disponibles:");
                        placesLabel.setBounds(30, 70, 150, 25);
                        JTextField placesField = new JTextField("33");
                        placesField.setBounds(180, 70, 150, 25);

                        JLabel prisesLabel = new JLabel("Places prises:");
                        prisesLabel.setBounds(30, 110, 150, 25);
                        JTextField prisesField = new JTextField("0");
                        prisesField.setBounds(180, 110, 150, 25);

                        JLabel idphLabel = new JLabel("ID_PH_D (optionnel):");
                        idphLabel.setBounds(30, 150, 150, 25);
                        JTextField idphField = new JTextField();
                        idphField.setBounds(180, 150, 150, 25);

                        JButton confirmBtn = new JButton("Ajouter");
                        confirmBtn.setBounds(130, 200, 120, 30);

                        addDomFrame.getContentPane().add(nomLabel);
                        addDomFrame.getContentPane().add(nomField);
                        addDomFrame.getContentPane().add(placesLabel);
                        addDomFrame.getContentPane().add(placesField);
                        addDomFrame.getContentPane().add(prisesLabel);
                        addDomFrame.getContentPane().add(prisesField);
                        addDomFrame.getContentPane().add(idphLabel);
                        addDomFrame.getContentPane().add(idphField);
                        addDomFrame.getContentPane().add(confirmBtn);

                        confirmBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String nom = nomField.getText().trim();
                                String dispoStr = placesField.getText().trim();
                                String prisesStr = prisesField.getText().trim();
                                String idphStr = idphField.getText().trim();

                                try {
                                    ConnectionDAO cdao = new ConnectionDAO();
                                    Connection conn = cdao.getConnection();
                                    Statement stmt = conn.createStatement();

                                    String sql;
                                    if (idphStr.isEmpty()) {
                                        sql = "INSERT INTO DOMINANTES (NOM, PLACESDISPO, PLACESPRISES, ID_PH_D) VALUES ('"
                                            + nom + "', " + dispoStr + ", " + prisesStr + ", NULL)";
                                    } else {
                                        sql = "INSERT INTO DOMINANTES (NOM, PLACESDISPO, PLACESPRISES, ID_PH_D) VALUES ('"
                                            + nom + "', " + dispoStr + ", " + prisesStr + ", " + idphStr + ")";
                                    }

                                    stmt.executeUpdate(sql);
                                    JOptionPane.showMessageDialog(addDomFrame, "✅ Dominante ajoutée avec succès !");
                                    addDomFrame.dispose();
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(addDomFrame, "❌ Erreur: " + ex.getMessage());
                                }
                            }
                        });

                        addDomFrame.setVisible(true);
                    }
                });
                JButton btnDeleteDominante = new JButton("Supprimer dominante");
                btnDeleteDominante.setBounds(100, 150, 200, 30);
                btnDeleteDominante.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame delDomFrame = new JFrame("Supprimer une dominante");
                        delDomFrame.setSize(350, 180);
                        delDomFrame.getContentPane().setLayout(null);

                        JLabel label = new JLabel("Nom de la dominante à supprimer :");
                        label.setBounds(20, 20, 300, 25);
                        delDomFrame.getContentPane().add(label);

                        JTextField nomField = new JTextField();
                        nomField.setBounds(20, 50, 290, 25);
                        delDomFrame.getContentPane().add(nomField);

                        JButton deleteBtn = new JButton("Supprimer");
                        deleteBtn.setBounds(100, 90, 120, 30);
                        delDomFrame.getContentPane().add(deleteBtn);

                        deleteBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String nom = nomField.getText().trim();

                                try {
                                    ConnectionDAO cdao = new ConnectionDAO();
                                    Connection conn = cdao.getConnection();
                                    Statement stmt = conn.createStatement();

                                    String sql = "DELETE FROM DOMINANTES WHERE NOM = '" + nom + "'";
                                    int rows = stmt.executeUpdate(sql);

                                    if (rows > 0) {
                                        JOptionPane.showMessageDialog(delDomFrame, "✅ Dominante supprimée avec succès !");
                                        delDomFrame.dispose();
                                    } else {
                                        JOptionPane.showMessageDialog(delDomFrame, "❌ Aucune dominante trouvée avec ce nom.");
                                    }
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(delDomFrame, "❌ Erreur: " + ex.getMessage());
                                }
                            }
                        });

                        delDomFrame.setVisible(true);
                    }
                });
                gestionFrame.getContentPane().add(btnAddStudent);
                gestionFrame.getContentPane().add(btnDeleteStudent);
                gestionFrame.getContentPane().add(btnAddDominante);
                gestionFrame.getContentPane().add(btnDeleteDominante);
                gestionFrame.setVisible(true);
            }
        });
        
        JButton btnAutoAffect = new JButton("Valider les choix remplis");
        btnAutoAffect.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnAutoAffect.setBounds(125, 359, 313, 56);
        jframe.getContentPane().add(btnAutoAffect);

        btnAutoAffect.addActionListener(e -> {
            try {
                Connection conn = ConnectionDAO.getConnection();
                conn.setAutoCommit(false);
                dao.ChoixDAO.validerChoixParRang(conn);  

                conn.commit();
                JOptionPane.showMessageDialog(jframe, "✅ Validation des choix réussie !");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(jframe, "❌ Erreur lors de la validation des choix.");
            }
        });
        
       
        
        
        
        
        JButton btnDistribuer3 = new JButton("Autodistribuer");
        btnDistribuer3.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnDistribuer3.setBounds(125, 466, 313, 56); 
        jframe.getContentPane().add(btnDistribuer3);

        btnDistribuer3.addActionListener(e -> {
            try {
                Connection conn = ConnectionDAO.getConnection();
                conn.setAutoCommit(false);

                dao.ChoixDAO.autoDistribuerSansChoix(conn);
                conn.commit();

                JOptionPane.showMessageDialog(jframe, "🎯 Autodistribution terminée !");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(jframe, "❌ Erreur dans l’autodistribution.");
            }
        });
        
 
        
        
        JButton btnForcer = new JButton("Forcer inscription");
        btnForcer.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnForcer.setBounds(125, 610, 313, 56);
        jframe.getContentPane().add(btnForcer);

        btnForcer.addActionListener(e -> {
            new ForcerInscriptionFrame();
        });
        
         
         JButton btnLancerProcedure = new JButton("Lancer Procedure");
         btnLancerProcedure.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent e) {
         		
         		 FenetreDates fenDates = new FenetreDates();
                 fenDates.setVisible(true);
             
         	}
         });
         btnLancerProcedure.setFont(new Font("Times New Roman", Font.BOLD, 18));
         btnLancerProcedure.setBounds(125, 547, 313, 56);
         jframe.getContentPane().add(btnLancerProcedure);

        

        
        jframe.setVisible(true);
    }
}        
