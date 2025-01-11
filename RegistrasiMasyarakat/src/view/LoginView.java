package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnGoToRegister;
    private JButton btnForgotPassword;
    private JLabel lblTitle;
    private JButton btnBack;

    public LoginView() {
        // Set up the frame
        setTitle("Login System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 660);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel with background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(230, 230, 230));

        // Back button panel
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new BorderLayout());
        backButtonPanel.setBackground(new Color(230, 230, 230));
        backButtonPanel.setBorder(new EmptyBorder(10, 20, 0, 20));

        btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBack.setForeground(new Color(1, 88, 88));
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFocusPainted(false);

        backButtonPanel.add(btnBack, BorderLayout.WEST);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(230, 230, 230));
        titlePanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        lblTitle = new JLabel("LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(51, 51, 51));
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(230, 230, 230));
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components initialization
        txtUsername = new JTextField(20);
        txtUsername.setPreferredSize(new Dimension(300, 40));
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        btnLogin = new JButton("LOGIN");
        btnLogin.setPreferredSize(new Dimension(300, 45));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(1, 88, 88));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);

        btnGoToRegister = new JButton("Don't have an account? Register");
        btnGoToRegister.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGoToRegister.setForeground(new Color(1, 88, 88));
        btnGoToRegister.setBorderPainted(false);
        btnGoToRegister.setContentAreaFilled(false);
        btnGoToRegister.setFocusPainted(false);

        btnForgotPassword = new JButton("Forgot Password?");
        btnForgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        btnForgotPassword.setForeground(new Color(1, 88, 88));
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setFocusPainted(false);

        // Add components to content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(new JLabel("Username"), gbc);
        gbc.gridy = 1;
        contentPanel.add(txtUsername, gbc);
        gbc.gridy = 2;
        contentPanel.add(new JLabel("Password"), gbc);
        gbc.gridy = 3;
        contentPanel.add(txtPassword, gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 10, 10, 10);
        contentPanel.add(btnLogin, gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 10, 10);
        contentPanel.add(btnGoToRegister, gbc);
        gbc.gridy = 6;
        contentPanel.add(btnForgotPassword, gbc);

        // Add panels to main panel
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    // Getters
    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    // Add action listeners
    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    public void addRegisterNavigationListener(ActionListener listener) {
        btnGoToRegister.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void addForgotPasswordListener(ActionListener listener) {
        btnForgotPassword.addActionListener(listener);
    }

    public void addBackButtonListener(ActionListener listener) {
        btnBack.addActionListener(listener);
    }
}
