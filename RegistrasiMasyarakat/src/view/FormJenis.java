package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import model.KategoriModel;

import java.util.ArrayList;
import java.util.List;

public class FormJenis extends JDialog {
    private JTextField namaField;
    private JComboBox<String> kategoriComboBox;
    private JButton saveButton;
    private JButton cancelButton;
    private List<KategoriModel> kategoriList;
    private Component parent;

    public FormJenis(JFrame parent, String title, List<KategoriModel> kategoriList) {
        super(parent, title, true);

        // Validate kategoriList
        if (kategoriList == null) {
            this.kategoriList = new ArrayList<>();
        } else {
            this.kategoriList = kategoriList;
        }

        // Initialize components
        namaField = new JTextField(20);
        kategoriComboBox = new JComboBox<>();

        // Populate combo box safely
        populateKategoriComboBox();

        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");

        // Layout setup
        setupLayout();
    }

    private void populateKategoriComboBox() {
        kategoriComboBox.removeAllItems(); // Clear existing items

        if (kategoriList != null && !kategoriList.isEmpty()) {
            for (KategoriModel kategori : kategoriList) {
                if (kategori != null && kategori.getNamaKategori() != null) {
                    kategoriComboBox.addItem(kategori.getNamaKategori());
                }
            }
        }

        // If no items were added, add a default item
        if (kategoriComboBox.getItemCount() == 0) {
            kategoriComboBox.addItem("Tidak ada kategori");
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to panel
        inputPanel.add(new JLabel("Nama Jenis:"));
        inputPanel.add(namaField);
        inputPanel.add(new JLabel("Kategori:"));
        inputPanel.add(kategoriComboBox);

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

    public String getNamaJenis() {
        return namaField.getText().trim();
    }

    public KategoriModel getSelectedKategori() {
        String selectedNamaKategori = (String) kategoriComboBox.getSelectedItem();
        if (selectedNamaKategori == null || selectedNamaKategori.equals("Tidak ada kategori")) {
            return null;
        }

        return kategoriList.stream()
                .filter(k -> k != null && selectedNamaKategori.equals(k.getNamaKategori()))
                .findFirst()
                .orElse(null);
    }

    public void setNamaJenis(String nama) {
        namaField.setText(nama != null ? nama : "");
    }

    public void setSelectedKategori(KategoriModel kategori) {
        if (kategori != null && kategori.getNamaKategori() != null) {
            kategoriComboBox.setSelectedItem(kategori.getNamaKategori());
        }
    }

    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
}