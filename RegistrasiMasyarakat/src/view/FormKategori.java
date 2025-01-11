package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FormKategori extends JDialog {
    private JTextField namaField;
    private JButton saveButton;
    private JButton cancelButton;

    public FormKategori(JFrame parent, String title) {
        super(parent, title, true);

        // Initialize components
        namaField = new JTextField(20);
        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");

        // Layout setup
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to panel
        inputPanel.add(new JLabel("Nama Kategori:"));
        inputPanel.add(namaField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Cancel button listener
        cancelButton.addActionListener(e -> dispose());

        // Set dialog properties
        pack();
        setLocationRelativeTo(parent);
    }

    public String getNamaKategori() {
        return namaField.getText().trim();
    }

    public void setNamaKategori(String nama) {
        namaField.setText(nama);
    }

    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
}