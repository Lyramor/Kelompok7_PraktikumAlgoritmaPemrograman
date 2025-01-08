package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OTPDialog extends JDialog {
    private JTextField txtOTP;
    private JButton btnVerify;
    private JButton btnCancel;
    private boolean verified = false;
    
    public OTPDialog(JFrame parent) {
        super(parent, "Enter OTP", true);
        initComponents();
    }
    
    private void initComponents() {
        setSize(300, 150);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // OTP input field
        txtOTP = new JTextField(15);
        txtOTP.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Buttons
        btnVerify = new JButton("Verify");
        btnCancel = new JButton("Cancel");
        
        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Enter OTP:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtOTP, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnVerify);
        buttonPanel.add(btnCancel);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Cancel button action
        btnCancel.addActionListener(e -> {
            verified = false;
            dispose();
        });
    }
    
    public void addVerifyListener(ActionListener listener) {
        btnVerify.addActionListener(listener);
    }
    
    public String getOTP() {
        return txtOTP.getText();
    }
    
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    
    public boolean isVerified() {
        return verified;
    }
}