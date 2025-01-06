package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class RegisterView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtEmail;
    private JButton btnRegister;
    private JButton btnBackToLogin;
    private JLabel lblTitle;
    
    public RegisterView() {
        setTitle("Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 660);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 245));
        titlePanel.setBorder(new EmptyBorder(50, 0, 30, 0));
        
        // Title Label
        lblTitle = new JLabel("REGISTER", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(51, 51, 51));
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        // Components with styling
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
            
        txtEmail = new JTextField(20);
        txtEmail.setPreferredSize(new Dimension(300, 40));
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(204, 204, 204)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        btnRegister = new JButton("REGISTER");
        btnRegister.setPreferredSize(new Dimension(300, 45));
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setBackground(new Color(70, 130, 180));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBorderPainted(false);
        btnRegister.setFocusPainted(false);
        
        btnBackToLogin = new JButton("Already have an account? Login");
        btnBackToLogin.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBackToLogin.setForeground(new Color(70, 130, 180));
        btnBackToLogin.setBorderPainted(false);
        btnBackToLogin.setContentAreaFilled(false);
        btnBackToLogin.setFocusPainted(false);
        
        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("Username"), gbc);
        
        gbc.gridy = 1;
        contentPanel.add(txtUsername, gbc);
        
        
        gbc.gridy = 2;
        contentPanel.add(new JLabel("Email"), gbc);
        
        gbc.gridy = 3;
        contentPanel.add(txtEmail, gbc);
        
        
        gbc.gridy = 4;
        contentPanel.add(new JLabel("Password"), gbc);
        
        gbc.gridy = 5;
        contentPanel.add(txtPassword, gbc);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 10, 10, 10);
        contentPanel.add(btnRegister, gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 10, 10, 10);
        contentPanel.add(btnBackToLogin, gbc);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
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
    
    public String getEmail() {
        return txtEmail.getText();
    }
    
    // Add action listeners
    public void addRegisterListener(ActionListener listener) {
        btnRegister.addActionListener(listener);
    }
    
    public void addLoginNavigationListener(ActionListener listener) {
        btnBackToLogin.addActionListener(listener);
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}