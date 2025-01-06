package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HalamanUtamaView extends JFrame {
    private JButton btnHalamanProfile;
    private JButton btnKategoriSampah;
    private JLabel lblTitle;

    public HalamanUtamaView() {
        // Set up the frame
        setTitle("E-Waste Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 660);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 245));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        lblTitle = new JLabel("E-Waste", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(51, 51, 51));
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        btnHalamanProfile = new JButton("Halaman Profile");
        btnHalamanProfile.setPreferredSize(new Dimension(300, 45));
        btnHalamanProfile.setFont(new Font("Arial", Font.BOLD, 14));
        btnHalamanProfile.setBackground(new Color(70, 130, 180));
        btnHalamanProfile.setForeground(Color.WHITE);
        btnHalamanProfile.setBorderPainted(false);
        btnHalamanProfile.setFocusPainted(false);

        btnKategoriSampah = new JButton("Kategori Sampah");
        btnKategoriSampah.setPreferredSize(new Dimension(300, 45));
        btnKategoriSampah.setFont(new Font("Arial", Font.BOLD, 14));
        btnKategoriSampah.setBackground(new Color(70, 130, 180));
        btnKategoriSampah.setForeground(Color.WHITE);
        btnKategoriSampah.setBorderPainted(false);
        btnKategoriSampah.setFocusPainted(false);

        // Add components to button panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnHalamanProfile, gbc);

        gbc.gridy = 1;
        buttonPanel.add(btnKategoriSampah, gbc);

        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Debugging: Output untuk memastikan listener dipasang
        System.out.println("View initialized");
    }

    // Add action listeners
    public void addProfileButtonListener(ActionListener listener) {
        System.out.println("Listener untuk Halaman Profile ditambahkan");
        btnHalamanProfile.addActionListener(listener);
    }

    public void addKategoriSampahButtonListener(ActionListener listener) {
        System.out.println("Listener untuk Kategori Sampah ditambahkan");
        btnKategoriSampah.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}