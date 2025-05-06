package GUI;

import dao.ConnectionDAO;
import dao.EtudiantDAO;
import model.Etudiants;
import stockage.StudentStatut;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.awt.Font;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import dao.ChoixDAO;

public class choix_FISA {

	
	    private JFrame jframe;

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	        	choix_FISA menu2 = new choix_FISA();
	            menu2.jframe.setVisible(true);
	        });
	    }

	    public choix_FISA(){
	        initialize();
	    }

	     private void initialize() {
	         jframe = new JFrame("Menu FISA");
	         jframe.getContentPane().setBackground(new Color(223, 209, 228));
	         jframe.setSize(500, 300);
	         jframe.getContentPane().setLayout(null);
	        
	         //tableau contenant le liste des dominantes
	         String[] tab_domin = {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"};
	         
	         JComboBox <String> comboBox = new JComboBox <> (tab_domin);
	        // comboBox.setModel(new DefaultComboBoxModel(new String[] {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"}));
	         comboBox.setBounds(93, 61, 258, 36);
	         jframe.getContentPane().add(comboBox);
	         
	         JComboBox <String> comboBox_1 = new JComboBox <> (tab_domin);
	        // comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"}));
	         comboBox_1.setBounds(93, 147, 258, 36);
	         jframe.getContentPane().add(comboBox_1);
	         
	         JLabel lblNewLabel = new JLabel("Choix 1");
	         lblNewLabel.setForeground(new Color(255, 0, 0));
	         lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
	         lblNewLabel.setBounds(38, 73, 45, 13);
	         jframe.getContentPane().add(lblNewLabel);
	         
	         JLabel lblChoix = new JLabel("Choix 2");
	         lblChoix.setForeground(new Color(255, 0, 0));
	         lblChoix.setFont(new Font("Times New Roman", Font.BOLD, 12));
	         lblChoix.setBounds(38, 159, 45, 13);
	         jframe.getContentPane().add(lblChoix);
	         
	         JLabel lblNewLabel_1 = new JLabel("      Faites vos choix selon vos préférences");
	         lblNewLabel_1.setForeground(new Color(128, 0, 128));
	         lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
	         lblNewLabel_1.setBounds(53, 10, 345, 25);
	         jframe.getContentPane().add(lblNewLabel_1);
	         
	         JButton btnNewButton = new JButton("VALIDER");
	         btnNewButton.setForeground(new Color(255, 255, 255));
	         btnNewButton.setBackground(new Color(0, 0, 64));
	         btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
	         btnNewButton.setBounds(167, 218, 99, 21);
	         jframe.getContentPane().add(btnNewButton);
	         
	         // enregistrement des choix dans la base de donnée après validation
	         
	         btnNewButton.addActionListener(new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	            	 
	            	 String username = StudentStatut.getIdStudent();
	                 // Récupère les choix
	                 List<String> choix = new ArrayList<>();
	                 choix.add(comboBox.getSelectedItem().toString());
	                 choix.add(comboBox_1.getSelectedItem().toString());

	                 //ChoixDAO.supprimerChoixEtudiant(username);
	                   ChoixDAO.validerChoix(username, choix);

	                 JOptionPane.showMessageDialog(jframe, "Choix enregistrés avec succès !");
	                 jframe.dispose();
	             }
	         });

	         
	         jframe.setVisible(true);
	         
	    }
}
