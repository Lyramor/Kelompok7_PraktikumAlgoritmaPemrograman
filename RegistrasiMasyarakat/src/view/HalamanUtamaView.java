package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HalamanUtamaView extends JFrame {
    private JButton btnHalamanProfile;
    private JButton btnKategoriSampah;
    private JButton btnJenisSampah;
    private JButton btnLogin;
    private JButton btnLogout;
    private JLabel titleLabel;
    private JLabel subtitleLabel;

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
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Vertical layout
        titlePanel.setBackground(new Color(245, 245, 245));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        // Add title and subtitle labels
        titleLabel = new JLabel("Welcome to eWaste!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitleLabel = new JLabel("Recycle. Reward. Repeat.", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add labels to title panel
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between labels
        titlePanel.add(subtitleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing before main title

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

        btnJenisSampah = new JButton("Jenis Sampah");
        btnJenisSampah.setPreferredSize(new Dimension(300, 45));
        btnJenisSampah.setFont(new Font("Arial", Font.BOLD, 14));
        btnJenisSampah.setBackground(new Color(70, 130, 180));
        btnJenisSampah.setForeground(Color.WHITE);
        btnJenisSampah.setBorderPainted(false);
        btnJenisSampah.setFocusPainted(false);

        // Add components to button panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnHalamanProfile, gbc);

        gbc.gridy = 1;
        buttonPanel.add(btnKategoriSampah, gbc);

        gbc.gridy = 2;
        buttonPanel.add(btnJenisSampah, gbc);

        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Bottom Panel for Login and Logout buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 2));
        bottomPanel.setBackground(new Color(245, 245, 245));

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);

        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setBackground(new Color(70, 130, 180));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);

        // Add buttons to bottom panel
        bottomPanel.add(btnLogin);
        bottomPanel.add(btnLogout);

        // Add bottom panel to main panel
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

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

    public void addJenisSampahButtonListener(ActionListener listener) {
        System.out.println("Listener untuk Jenis Sampah ditambahkan");
        btnJenisSampah.addActionListener(listener);
    }

    public void addLoginButtonListener(ActionListener listener) {
        System.out.println("Listener untuk Login ditambahkan");
        btnLogin.addActionListener(listener);
    }

    public void addLogoutButtonListener(ActionListener listener) {
        System.out.println("Listener untuk Logout ditambahkan");
        btnLogout.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
