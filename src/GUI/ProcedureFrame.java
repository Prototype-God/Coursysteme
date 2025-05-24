package GUI;


	import java.sql.Connection;
	import java.sql.Statement;
	import java.sql.ResultSet;
	import javax.swing.*;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;
	import dao.ConnectionDAO;
	import java.sql.PreparedStatement;
	import java.awt.*;

	public class ProcedureFrame {

	    private JFrame frame;

	    public ProcedureFrame() {
	        frame = new JFrame("Gestion des Dominantes");
	        frame.setSize(500, 300);
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        frame.setLayout(null);

	        JLabel titreLabel = new JLabel("Configurer l'accès d'une dominante");
	        titreLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
	        titreLabel.setBounds(80, 20, 400, 30);
	        frame.add(titreLabel);

	        JLabel dominanteLabel = new JLabel("Dominante :");
	        dominanteLabel.setBounds(50, 80, 100, 25);
	        frame.add(dominanteLabel);

	        JComboBox<String> comboDominante = new JComboBox<>();

	        try (Connection conn = ConnectionDAO.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("SELECT NOM FROM DOMINANTES")) {

	            while (rs.next()) {
	                comboDominante.addItem(rs.getString("NOM"));
	            }

	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(frame, "Erreur de chargement des dominantes !");
	        }

	        
	        
	        
	     
	        
	        comboDominante.setBounds(150, 80, 250, 25);
	        frame.add(comboDominante);
	        JLabel ouvertLabel = new JLabel("Ouvert à :");
	        ouvertLabel.setBounds(50, 130, 100, 25);
	        frame.add(ouvertLabel);

	        String[] ouvertOptions = {"Aucun", "FISE", "FISA", "Les deux"};
	        JComboBox<String> comboOuvert = new JComboBox<>(ouvertOptions);
	        comboOuvert.setBounds(150, 130, 250, 25);
	        frame.add(comboOuvert);

	     
	        JLabel fermerLabel = new JLabel("Fermer à :");
	        fermerLabel.setBounds(50, 170, 100, 25);
	        frame.add(fermerLabel);

	        String[] fermerOptions = {"Aucun", "FISE", "FISA", "Les deux"};
	        JComboBox<String> comboFermer = new JComboBox<>(fermerOptions);
	        comboFermer.setBounds(150, 170, 250, 25);
	        frame.add(comboFermer);

	        
	        
	        
	        
	        
	        JButton validerBtn = new JButton("Valider");
	        validerBtn.setBounds(180, 200, 120, 30);
	        frame.add(validerBtn);
	        frame.setVisible(true);
	        validerBtn.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                String dominanteChoisie = comboDominante.getSelectedItem().toString();
	                String ouvertChoix = comboOuvert.getSelectedItem().toString();
	                String fermerChoix = comboFermer.getSelectedItem().toString();

	                int idPhD = -1;

	                if (ouvertChoix.equals("FISE") && fermerChoix.equals("FISA")) {
	                    idPhD = 1;
	                } else if (ouvertChoix.equals("FISA") && fermerChoix.equals("FISE")) {
	                    idPhD = 2;
	                } else if (ouvertChoix.equals("Les deux") && fermerChoix.equals("Aucun")) {
	                    idPhD = 3;
	                } else if (ouvertChoix.equals("Aucun") && fermerChoix.equals("Les deux")) {
	                    idPhD = 4;
	                } else {
	                    JOptionPane.showMessageDialog(frame, "❌ Combinaison invalide de Ouvert et Fermer !");
	                    return;
	                }

	                // Mise à jour en base
	                try (Connection conn = ConnectionDAO.getConnection();
	                     PreparedStatement stmt = conn.prepareStatement(
	                             "UPDATE DOMINANTES SET ID_PH_D = ? WHERE NOM = ?")) {

	                    stmt.setInt(1, idPhD);
	                    stmt.setString(2, dominanteChoisie);
	                    int rows = stmt.executeUpdate();

	                    if (rows > 0) {
	                        JOptionPane.showMessageDialog(frame, "✅ Dominante mise à jour avec succès !");
	                    } else {
	                        JOptionPane.showMessageDialog(frame, "⚠️ Aucune dominante mise à jour !");
	                    }

	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(frame, "❌ Erreur lors de la mise à jour.");
	                }
	            }
	        });
	    }
	}


