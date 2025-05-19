package GUI;


import dao.EtudiantDAO;
import stockage.StudentStatut;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Font;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import dao.ChoixDAO;
import dao.ConnectionDAO;

public class choix_FISE {
	
	    private JFrame jframe1;

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	        	choix_FISE menu3 = new choix_FISE();
	            menu3.jframe1.setVisible(true);
	        });
	    }

	    public choix_FISE(){
	    	try (Connection conn = ConnectionDAO.getConnection()) {
	    	    PreparedStatement stmt = conn.prepareStatement(
	    	        "SELECT FISE_OUVERT, FISE_DEBUT, FISE_FIN FROM PROCEDURE_STATUT ORDER BY ID DESC FETCH FIRST 1 ROWS ONLY"
	    	    );
	    	    ResultSet rs = stmt.executeQuery();

	    	    if (rs.next()) {
	    	        int ouvert = rs.getInt("FISE_OUVERT");
	    	        Date debut = rs.getDate("FISE_DEBUT");
	    	        Date fin = rs.getDate("FISE_FIN");
	    	        Date now = new Date(System.currentTimeMillis());

	    	        if (!(ouvert == 1 && !now.before(debut) && !now.after(fin))) {
	    	            JOptionPane.showMessageDialog(null, "❌ La période de choix FISE n’est pas active.");
	    	            return;
	    	        }
	    	    } else {
	    	        JOptionPane.showMessageDialog(null, "❌ Aucune période définie pour FISE.");
	    	        return;
	    	    }
	    	} catch (Exception e) {
	    	    e.printStackTrace();
	    	    JOptionPane.showMessageDialog(null, "❌ Erreur lors de la vérification de la période.");
	    	    return;
	    	}

	        initialize();
	    }
	    
	    
	     private void initialize() {
	         jframe1 = new JFrame("Menu FISE");
	         jframe1.getContentPane().setBackground(new Color(223, 209, 228));
	         jframe1.setSize(500, 600);
	         jframe1.getContentPane().setLayout(null);
	         
	         //String[] tab_domin = {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"};
	        
	         List<String> dominantes = EtudiantDAO.getAllDominantes();
	         String[] tab_domin = dominantes.toArray(new String[0]);
	         
	         JComboBox <String> comboBox = new JComboBox <>(tab_domin);
	        // comboBox.setModel(new DefaultComboBoxModel(new String[] {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"}));
	          comboBox.setBounds(103, 61, 258, 36);
	         jframe1.getContentPane().add(comboBox);
	         
	         JComboBox  <String> comboBox_1 = new JComboBox <> (tab_domin);
	        // comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"}));
	         comboBox_1.setBounds(103, 231, 258, 36);
	         jframe1.getContentPane().add(comboBox_1);
	         
	         JLabel lblNewLabel = new JLabel("Choix 1");
	         lblNewLabel.setForeground(new Color(255, 0, 0));
	         lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
	         lblNewLabel.setBounds(38, 73, 45, 13);
	         jframe1.getContentPane().add(lblNewLabel);
	         
	         JLabel lblChoix = new JLabel("Choix 2");
	         lblChoix.setForeground(new Color(255, 0, 0));
	         lblChoix.setFont(new Font("Times New Roman", Font.BOLD, 12));
	         lblChoix.setBounds(38, 167, 45, 13);
	         jframe1.getContentPane().add(lblChoix);
	         
	         JLabel lblNewLabel_1 = new JLabel("      Faites vos choix selon vos préférences");
	         lblNewLabel_1.setForeground(new Color(128, 0, 128));
	         lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
	         lblNewLabel_1.setBounds(59, 10, 345, 25);
	         jframe1.getContentPane().add(lblNewLabel_1);
	         
	         JComboBox  <String> comboBox_2 = new JComboBox  <> (tab_domin);
	         //comboBox_1_1.setModel(new DefaultComboBoxModel(new String[] {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"}));
	         comboBox_2.setBounds(103, 155, 258, 36);
	         jframe1.getContentPane().add(comboBox_2);
	         
	         JComboBox <String> comboBox_3 = new JComboBox  <> (tab_domin);
	         //comboBox_1_2.setModel(new DefaultComboBoxModel(new String[] {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"}));
	         comboBox_3.setBounds(103, 320, 258, 36);
	         jframe1.getContentPane().add(comboBox_3);
	         
	         JComboBox <String> comboBox_4 = new JComboBox  <> (tab_domin);
	        // comboBox_1_3.setModel(new DefaultComboBoxModel(new String[] {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"}));
	         comboBox_4.setBounds(103, 405, 258, 36);
	         jframe1.getContentPane().add(comboBox_4);
	         
	         JLabel lblChoix_2 = new JLabel("Choix 3");
	         lblChoix_2.setForeground(Color.RED);
	         lblChoix_2.setFont(new Font("Times New Roman", Font.BOLD, 12));
	         lblChoix_2.setBounds(38, 243, 45, 13);
	         jframe1.getContentPane().add(lblChoix_2);
	         
	         JLabel lblChoix_1 = new JLabel("Choix 4");
	         lblChoix_1.setForeground(Color.RED);
	         lblChoix_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
	         lblChoix_1.setBounds(38, 332, 45, 13);
	         jframe1.getContentPane().add(lblChoix_1);
	         
	         JLabel lblChoix_4 = new JLabel("Choix 5");
	         lblChoix_4.setForeground(Color.RED);
	         lblChoix_4.setFont(new Font("Times New Roman", Font.BOLD, 12));
	         lblChoix_4.setBounds(38, 417, 45, 13);
	         jframe1.getContentPane().add(lblChoix_4);
	         
	         JButton btnNewButton = new JButton("VALIDER");
	         btnNewButton.setForeground(new Color(255, 255, 255));
	         btnNewButton.setBackground(new Color(64, 0, 64));
	         btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
	         btnNewButton.setBounds(288, 490, 116, 21);
	         jframe1.getContentPane().add(btnNewButton);
	         
	         JButton btnModifier = new JButton("MODIFIER");
	         btnModifier.setForeground(Color.WHITE);
	         btnModifier.setFont(new Font("Times New Roman", Font.BOLD, 14));
	         btnModifier.setBackground(new Color(64, 0, 64));
	         btnModifier.setBounds(59, 491, 116, 21);
	         jframe1.getContentPane().add(btnModifier);
	         
	         btnNewButton.addActionListener(new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	            	 
	         String username = StudentStatut.getIdStudent();
	         
	         // enregistrement ou récupération des choix
	         List<String> choixDominantes = new ArrayList<>();
	            choixDominantes.add(comboBox.getSelectedItem().toString());
	            choixDominantes.add(comboBox_1.getSelectedItem().toString());
	            choixDominantes.add(comboBox_2.getSelectedItem().toString());
	            choixDominantes.add(comboBox_3.getSelectedItem().toString());
	            choixDominantes.add(comboBox_4.getSelectedItem().toString());
	            // On empêche l'étudiant d'effectuer plusieurs fois le même choix
	            
	            boolean doublon=false;
	            for (int i=0; i < choixDominantes.size(); i++) {
	            	for(int j=i+1; j  < choixDominantes.size(); j++)
	            	{
	            	if (choixDominantes.get(i).equals(choixDominantes.get(j))) {
	            		  doublon = true;
	            		  break;
	            	     }
	                }
	            if(doublon) break;	
	            }
	            
	            if(doublon) {
	            JOptionPane.showMessageDialog(jframe1, "Vous ne pouvez pas choisir plusieurs fois la même dominante!\n             Modifiez vos choix");
                return;
	            }
	           
	            // validation des choix
	            ChoixDAO.validerChoix(username, choixDominantes);
	            JOptionPane.showMessageDialog(jframe1, "Choix enregistrés avec succès !");
                jframe1.dispose();
            }
        });

	         
	         jframe1.setVisible(true);
	         
	    }
}

