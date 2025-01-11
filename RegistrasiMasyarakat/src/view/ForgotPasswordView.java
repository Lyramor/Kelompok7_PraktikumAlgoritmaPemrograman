package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class ForgotPasswordView extends JFrame {
    private JTextField txtEmail;
    private JButton btnSendCode;
    private JButton btnBackToLogin;
    private JPanel mainPanel;
    
    public ForgotPasswordView() {
        setTitle("Forgot Password");
        setSize(420, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Main Panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(230, 230, 230)); // Warna latar belakang utama
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(230, 230, 230)); // Warna latar belakang untuk konten
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(230, 230, 230)); // Warna latar belakang untuk judul
        titlePanel.setBorder(new EmptyBorder(50, 0, 30, 0));
        
        // Title Label
        JLabel lblTitle = new JLabel("FORGOT PASSWORD", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(51, 51, 51));
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        // Components
        txtEmail = new JTextField(20);
        txtEmail.setPreferredSize(new Dimension(300, 40));
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(204, 204, 204)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            
        btnSendCode = new JButton("Send Reset Code");
        btnSendCode.setPreferredSize(new Dimension(300, 45));
        btnSendCode.setFont(new Font("Arial", Font.BOLD, 14));
        btnSendCode.setBackground(new Color(1, 88, 88)); // Warna tombol
        btnSendCode.setForeground(Color.WHITE);
        btnSendCode.setBorderPainted(false);
        btnSendCode.setFocusPainted(false);
        
        btnBackToLogin = new JButton("Back to Login");
        btnBackToLogin.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBackToLogin.setForeground(new Color(70, 130, 180));
        btnBackToLogin.setForeground(new Color(1, 88, 88));
        btnBackToLogin.setBorderPainted(false);
        btnBackToLogin.setContentAreaFilled(false);
        btnBackToLogin.setFocusPainted(false);
        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("Enter your email:"), gbc);
        
        gbc.gridy = 1;
        contentPanel.add(txtEmail, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        contentPanel.add(btnSendCode, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 10, 10, 10);
        contentPanel.add(btnBackToLogin, gbc);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    public String getEmail() {
        return txtEmail.getText();
    }
    
    public void addSendCodeListener(ActionListener listener) {
        btnSendCode.addActionListener(listener);
    }
    
    public void addBackToLoginListener(ActionListener listener) {
        btnBackToLogin.addActionListener(listener);
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
