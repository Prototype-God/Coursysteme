package GUI;


import dao.EtudiantDAO;
import stockage.StudentStatut;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import dao.ChoixDAO;
import javax.swing.GroupLayout.Alignment;

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
	        
	         //tableau contenant le liste des dominantes
	         //String[] tab_domin = {"ESAA", "DARIA", "EDD", "IA-DES", "GET", "BDTN", "CERT", "IA-IR", "IF", "ISN", "IABD", "DLTQ", "ISEMAC", "MCTSE"};
	         
	         List<String> dominantes = EtudiantDAO.getAllDominantes();
	         String[] tab_domin = dominantes.toArray(new String[0]);
	         
	         JComboBox <String> comboBox = new JComboBox <> (tab_domin);
	         
	         JComboBox <String> comboBox_1 = new JComboBox <> (tab_domin);
	         
	         JLabel lblNewLabel = new JLabel("Choix 1");
	         lblNewLabel.setForeground(new Color(255, 0, 0));
	         lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
	         
	         JLabel lblChoix = new JLabel("Choix 2");
	         lblChoix.setForeground(new Color(255, 0, 0));
	         lblChoix.setFont(new Font("Times New Roman", Font.BOLD, 12));
	         
	         JLabel lblNewLabel_1 = new JLabel("      Faites vos choix selon vos préférences");
	         lblNewLabel_1.setForeground(new Color(128, 0, 128));
	         lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
	         
	         JButton btnNewButton = new JButton("VALIDER");
	         btnNewButton.setForeground(new Color(255, 255, 255));
	         btnNewButton.setBackground(new Color(0, 0, 64));
	         btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
	         
	         JButton btnModifier = new JButton("MODIFIER");
	         btnModifier.setForeground(Color.WHITE);
	         btnModifier.setFont(new Font("Times New Roman", Font.BOLD, 14));
	         btnModifier.setBackground(new Color(0, 0, 64));
	         GroupLayout groupLayout = new GroupLayout(jframe.getContentPane());
	         groupLayout.setHorizontalGroup(
	         	groupLayout.createParallelGroup(Alignment.LEADING)
	         		.addGroup(groupLayout.createSequentialGroup()
	         			.addGap(53)
	         			.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 345, GroupLayout.PREFERRED_SIZE))
	         		.addGroup(groupLayout.createSequentialGroup()
	         			.addGap(38)
	         			.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
	         			.addGap(10)
	         			.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE))
	         		.addGroup(groupLayout.createSequentialGroup()
	         			.addGap(38)
	         			.addComponent(lblChoix, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
	         			.addGap(10)
	         			.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE))
	         		.addGroup(groupLayout.createSequentialGroup()
	         			.addGap(68)
	         			.addComponent(btnModifier, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
	         			.addGap(114)
	         			.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE))
	         );
	         groupLayout.setVerticalGroup(
	         	groupLayout.createParallelGroup(Alignment.LEADING)
	         		.addGroup(groupLayout.createSequentialGroup()
	         			.addGap(10)
	         			.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
	         			.addGap(26)
	         			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	         				.addGroup(groupLayout.createSequentialGroup()
	         					.addGap(12)
	         					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE))
	         				.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
	         			.addGap(50)
	         			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	         				.addGroup(groupLayout.createSequentialGroup()
	         					.addGap(12)
	         					.addComponent(lblChoix, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE))
	         				.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
	         			.addGap(35)
	         			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	         				.addGroup(groupLayout.createSequentialGroup()
	         					.addGap(1)
	         					.addComponent(btnModifier, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
	         				.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
	         );
	         jframe.getContentPane().setLayout(groupLayout);
	         
	         // enregistrement des choix dans la base de donnée après validation
	         
	         btnNewButton.addActionListener(new ActionListener() {
	            
	        	 public void actionPerformed(ActionEvent e) {
	            	 
	            	 String username = StudentStatut.getIdStudent();
	                 // On récupère les choix des étudiants
	                 List<String> choix = new ArrayList<>();
	                 choix.add(comboBox.getSelectedItem().toString());
	                 choix.add(comboBox_1.getSelectedItem().toString());
                      
	              // On empêche l'étudiant de choisir plusieurs fois la même dominante 
	                 if (choix.get(0).equals(choix.get(1))) {
	                     JOptionPane.showMessageDialog(jframe, "Vous ne pouvez pas choisir plusieurs fois la même dominante!\n             Modifiez vos choix");
	                     return;
	                 }
	             
	               
	              //validation des choix de l'étudiant   
	                 ChoixDAO.validerChoix(username, choix);

	                 JOptionPane.showMessageDialog(jframe, "Choix enregistrés avec succès !");
	                 jframe.dispose();
	             }
	         });

	         btnModifier.addActionListener(new ActionListener() {
	        	 
	        	 public void actionPerformed(ActionEvent e) {
	        		 
	        		 
	        	 }
	         });
	         jframe.setVisible(true);
	         
	    }
}
