package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.awt.event.*;
import dao.ProcedureDAO;

public class FenetreDates extends JFrame {

    public FenetreDates() {
        setTitle("Gestion des périodes FISE/FISA");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelDates = new JPanel(new GridLayout(5, 2, 10, 10));
        panelDates.setBorder(BorderFactory.createTitledBorder("⏳ Gérer les périodes de choix"));

        SpinnerDateModel modelFiseDebut = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        SpinnerDateModel modelFiseFin = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        SpinnerDateModel modelFisaDebut = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        SpinnerDateModel modelFisaFin = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);

        JSpinner spinnerFiseDebut = new JSpinner(modelFiseDebut);
        JSpinner spinnerFiseFin = new JSpinner(modelFiseFin);
        JSpinner spinnerFisaDebut = new JSpinner(modelFisaDebut);
        JSpinner spinnerFisaFin = new JSpinner(modelFisaFin);

        // Format affichage date
        spinnerFiseDebut.setEditor(new JSpinner.DateEditor(spinnerFiseDebut, "yyyy-MM-dd"));
        spinnerFiseFin.setEditor(new JSpinner.DateEditor(spinnerFiseFin, "yyyy-MM-dd"));
        spinnerFisaDebut.setEditor(new JSpinner.DateEditor(spinnerFisaDebut, "yyyy-MM-dd"));
        spinnerFisaFin.setEditor(new JSpinner.DateEditor(spinnerFisaFin, "yyyy-MM-dd"));

        JButton btnValiderFise = new JButton("✅ Valider FISE");
        JButton btnValiderFisa = new JButton("✅ Valider FISA");

        panelDates.add(new JLabel("FISE Début :"));
        panelDates.add(spinnerFiseDebut);
        panelDates.add(new JLabel("FISE Fin :"));
        panelDates.add(spinnerFiseFin);
        panelDates.add(new JLabel("FISA Début :"));
        panelDates.add(spinnerFisaDebut);
        panelDates.add(new JLabel("FISA Fin :"));
        panelDates.add(spinnerFisaFin);
        panelDates.add(btnValiderFise);
        panelDates.add(btnValiderFisa);

        add(panelDates);

        // Actions boutons
        btnValiderFise.addActionListener(e -> {
            Date debut = modelFiseDebut.getDate();
            Date fin = modelFiseFin.getDate();
            if (debut.after(fin)) {
                JOptionPane.showMessageDialog(this, "❌ La date de début doit être avant la date de fin.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ProcedureDAO.setFiseDates(debut, fin);
            JOptionPane.showMessageDialog(this, "✅ Dates FISE enregistrées !");
        });

        btnValiderFisa.addActionListener(e -> {
            Date debut = modelFisaDebut.getDate();
            Date fin = modelFisaFin.getDate();
            if (debut.after(fin)) {
                JOptionPane.showMessageDialog(this, "❌ La date de début doit être avant la date de fin.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ProcedureDAO.setFisaDates(debut, fin);
            JOptionPane.showMessageDialog(this, "✅ Dates FISA enregistrées !");
        });
    }
}