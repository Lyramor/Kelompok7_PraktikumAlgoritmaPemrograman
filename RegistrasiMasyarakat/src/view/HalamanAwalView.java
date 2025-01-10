package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HalamanAwalView extends JFrame {
    private JButton btnLogin;
    private JButton btnRegister;
    private JLabel lblTitle;

    public HalamanAwalView() {
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

        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(300, 45));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);

        btnRegister = new JButton("Register");
        btnRegister.setPreferredSize(new Dimension(300, 45));
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setBackground(new Color(70, 130, 180));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBorderPainted(false);
        btnRegister.setFocusPainted(false);

        // Add components to button panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnLogin, gbc);

        gbc.gridy = 1;
        buttonPanel.add(btnRegister, gbc);

        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Debugging: Output untuk memastikan listener dipasang
        System.out.println("Halaman Awal View initialized");
    }

    // Add action listeners
    public void addLoginButtonListener(ActionListener listener) {
        System.out.println("Listener untuk Login ditambahkan");
        btnLogin.addActionListener(listener);
    }

    public void addRegisterButtonListener(ActionListener listener) {
        System.out.println("Listener untuk Register ditambahkan");
        btnRegister.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
