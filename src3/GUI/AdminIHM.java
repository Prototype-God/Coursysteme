package GUI;

import dao.ConnectionDAO;
import dao.EtudiantDAO;
import model.Etudiants;

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
        jframe.setBounds(100, 100, 600, 650);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("MENU");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 38));
        lblNewLabel.setBounds(210, 42, 167, 92);
        jframe.getContentPane().add(lblNewLabel);

        
        
        // premier ponction
        JButton btnNewButton = new JButton("SUIVI DES CHOIX");
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnNewButton.setBounds(125, 166, 313, 56);
        jframe.getContentPane().add(btnNewButton);

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame saisieFrame = new JFrame("Rechercher un étudiant");
                saisieFrame.setSize(400, 300);
                saisieFrame.setLayout(null);

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

                saisieFrame.add(nomLabel);
                saisieFrame.add(nomField);
                saisieFrame.add(prenomLabel);
                saisieFrame.add(prenomField);
                saisieFrame.add(searchBtn);

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
        btnNewButton_1.setBounds(125, 273, 313, 56);
        jframe.getContentPane().add(btnNewButton_1);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ProcedureFrame(); 
            }
        });

        JButton btnNewButton_2 = new JButton("MODIFIER LES INFORMATIONS");
        btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnNewButton_2.setBounds(125, 371, 311, 56);
        jframe.getContentPane().add(btnNewButton_2);


        btnNewButton_2 .addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame gestionFrame = new JFrame("SAISIR DES INFORMATIONS");
                gestionFrame.setSize(400, 300);
                gestionFrame.setLayout(null);

                JButton btnAddStudent = new JButton("Ajouter étudiant");
                btnAddStudent.setBounds(100, 30, 200, 30);
                btnAddStudent.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFrame addFrame = new JFrame("Ajouter un étudiant");
                        addFrame.setSize(400, 440);
                        addFrame.setLayout(null);

                        String[] labels = { "Username", "Nom", "Prénom", "Filière", "Promo", "Date naissance (YYYY-MM-DD)", "Password", "Rang","ID_PH"  };
                        JTextField[] fields = new JTextField[labels.length];

                        for (int i = 0; i < labels.length; i++) {
                            JLabel label = new JLabel(labels[i] + ":");
                            label.setBounds(30, 30 + i * 40, 180, 25);
                            addFrame.add(label);

                            fields[i] = new JTextField();
                            fields[i].setBounds(200, 30 + i * 40, 150, 25);
                            addFrame.add(fields[i]);
                        }

                        JButton confirmBtn = new JButton("Ajouter");
                        confirmBtn.setBounds(130, 370, 120, 30);
                        addFrame.add(confirmBtn);

                        confirmBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String username = fields[0].getText().trim();
                                String nom = fields[1].getText().trim();
                                String prenom = fields[2].getText().trim();
                                String filiere = fields[3].getText().trim();
                                String promo = fields[4].getText().trim();
                                String dateNaiss = fields[5].getText().trim();
                                String password = fields[6].getText().trim();
                                String rangStr = fields[7].getText().trim();

                                try {
                                    int rang = Integer.parseInt(rangStr);
                                    Connection conn = ConnectionDAO.getConnection();
                                    Statement stmt = conn.createStatement();
                                    String sql = "INSERT INTO ETUDIANT (USERNAME, NOM, PRENOM, FILIERE, PROMO, DATENAISSANCE, PASSWORD, RANG,ID_PH) " +
                                            "VALUES ('" + username + "', '" + nom + "', '" + prenom + "', '" + filiere + "', " + promo + ", " +
                                            "TO_DATE('" + dateNaiss + "', 'YYYY-MM-DD'), '" + password + "', " + rang + ", 0)";
                                    stmt.executeUpdate(sql);
                                    JOptionPane.showMessageDialog(addFrame, "✅ Étudiant ajouté avec succès !");
                                    addFrame.dispose();
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
                        delFrame.setLayout(null);

                        JLabel userLabel = new JLabel("Username de l'étudiant à supprimer :");
                        userLabel.setBounds(20, 20, 300, 25);
                        delFrame.add(userLabel);

                        JTextField userField = new JTextField();
                        userField.setBounds(20, 50, 290, 25);
                        delFrame.add(userField);

                        JButton deleteBtn = new JButton("Supprimer");
                        deleteBtn.setBounds(100, 90, 120, 30);
                        delFrame.add(deleteBtn);

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
                        addDomFrame.setLayout(null);

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

                        addDomFrame.add(nomLabel);
                        addDomFrame.add(nomField);
                        addDomFrame.add(placesLabel);
                        addDomFrame.add(placesField);
                        addDomFrame.add(prisesLabel);
                        addDomFrame.add(prisesField);
                        addDomFrame.add(idphLabel);
                        addDomFrame.add(idphField);
                        addDomFrame.add(confirmBtn);

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
                        delDomFrame.setLayout(null);

                        JLabel label = new JLabel("Nom de la dominante à supprimer :");
                        label.setBounds(20, 20, 300, 25);
                        delDomFrame.add(label);

                        JTextField nomField = new JTextField();
                        nomField.setBounds(20, 50, 290, 25);
                        delDomFrame.add(nomField);

                        JButton deleteBtn = new JButton("Supprimer");
                        deleteBtn.setBounds(100, 90, 120, 30);
                        delDomFrame.add(deleteBtn);

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
                gestionFrame.add(btnAddStudent);
                gestionFrame.add(btnDeleteStudent);
                gestionFrame.add(btnAddDominante);
                gestionFrame.add(btnDeleteDominante);
                gestionFrame.setVisible(true);
            }
        });
        
        
        JButton btnAutoAffect = new JButton("Valider");
        btnAutoAffect.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnAutoAffect.setBounds(125, 470, 313, 56);
        jframe.getContentPane().add(btnAutoAffect);

        btnAutoAffect.addActionListener(e -> {
            try {
                Connection conn = ConnectionDAO.getConnection();
                conn.setAutoCommit(false);

                dao.ChoixDAO.autoDistribuerSansChoix(conn);

                conn.commit(); 
                JOptionPane.showMessageDialog(jframe, "✅ Attribution automatique terminée !");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(jframe, "❌ Erreur lors de l'attribution.");
            }
        });
        
       
        
        
        
        
        JButton btnDistribuer3 = new JButton("Autodistribuer");
        btnDistribuer3.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnDistribuer3.setBounds(125, 540, 313, 56); 
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
        jframe.setVisible(true);
        
        JButton btnLancerProc = new JButton("Lancer la procédure");
        btnLancerProc.setBounds(125, 690, 250, 40);
        btnLancerProc.setFont(new Font("Times New Roman", Font.BOLD, 16));
        jframe.getContentPane().add(btnLancerProc);
        btnLancerProc.addActionListener(e -> {
            JFrame procFrame = new JFrame("Lancement de procédure");
            procFrame.setSize(400, 300);
            procFrame.setLayout(null);

            JLabel lblDebut = new JLabel("Date de début (YYYY-MM-DD) :");
            lblDebut.setBounds(30, 30, 200, 25);
            procFrame.add(lblDebut);

            JTextField dateDebutField = new JTextField();
            dateDebutField.setBounds(220, 30, 130, 25);
            procFrame.add(dateDebutField);

            JLabel lblFin = new JLabel("Date de fin (YYYY-MM-DD) :");
            lblFin.setBounds(30, 70, 200, 25);
            procFrame.add(lblFin);

            JTextField dateFinField = new JTextField();
            dateFinField.setBounds(220, 70, 130, 25);
            procFrame.add(dateFinField);

            JCheckBox checkFISE = new JCheckBox("FISE");
            checkFISE.setBounds(100, 110, 80, 25);
            procFrame.add(checkFISE);

            JCheckBox checkFISA = new JCheckBox("FISA");
            checkFISA.setBounds(200, 110, 80, 25);
            procFrame.add(checkFISA);

            JButton validerBtn = new JButton("Valider");
            validerBtn.setBounds(140, 160, 100, 30);
            procFrame.add(validerBtn);

            validerBtn.addActionListener(ev -> {
                String dateDebut = dateDebutField.getText().trim();
                String dateFin = dateFinField.getText().trim();
                boolean fise = checkFISE.isSelected();
                boolean fisa = checkFISA.isSelected();

                if (dateDebut.isEmpty() || dateFin.isEmpty() || (!fise && !fisa)) {
                    JOptionPane.showMessageDialog(procFrame, "Veuillez remplir toutes les informations.");
                    return;
                }

                try (Connection conn = ConnectionDAO.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO PROCEDURE_STATUT " +
                        "(ID, FISE_OUVERT, FISE_DEBUT, FISE_FIN, FISA_OUVERT, FISA_DEBUT, FISA_FIN) " +
                        "VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'))"
                    );

                    stmt.setInt(1, 1); 
                    stmt.setInt(2, fise ? 1 : 0); 
                    stmt.setString(3, fise ? dateDebut : null); 
                    stmt.setString(4, fise ? dateFin : null);   
                    stmt.setInt(5, fisa ? 1 : 0); 
                    stmt.setString(6, fisa ? dateDebut : null); 
                    stmt.setString(7, fisa ? dateFin : null);  

                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(procFrame, "✅ Procédure enregistrée !");
                    procFrame.dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(procFrame, "❌ Erreur lors de l'enregistrement.");
                }
            });

            procFrame.setVisible(true);
        });
 
    }
}
