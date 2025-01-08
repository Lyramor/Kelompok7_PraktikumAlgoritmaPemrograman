package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ResetPasswordDialog extends JDialog {
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtResetCode;
    private JButton btnReset;
    private JButton btnCancel;
    private boolean resetSuccess = false;

    public ResetPasswordDialog(JFrame parent) {
        super(parent, "Reset Password", true);
        initComponents();
    }

    private void initComponents() {
        setSize(400, 300); // Perbesar ukuran dialog
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Tambahkan margin antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL; // Komponen akan mengisi ruang horizontal

        // Components
        txtResetCode = new JTextField();
        txtNewPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        btnReset = new JButton("Reset Password");
        btnCancel = new JButton("Cancel");

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // Label tidak memperluas ruang
        panel.add(new JLabel("Reset Code:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1; // Field input diperluas
        panel.add(txtResetCode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtNewPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtConfirmPassword, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnReset);
        buttonPanel.add(btnCancel);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
    }

    
    public String getResetCode() {
        return txtResetCode.getText();
    }
    
    public String getNewPassword() {
        return new String(txtNewPassword.getPassword());
    }
    
    public String getConfirmPassword() {
        return new String(txtConfirmPassword.getPassword());
    }
    
    public void addResetListener(ActionListener listener) {
        btnReset.addActionListener(listener);
    }
    
    public void setResetSuccess(boolean success) {
        this.resetSuccess = success;
    }
    
    public boolean isResetSuccess() {
        return resetSuccess;
    }
}