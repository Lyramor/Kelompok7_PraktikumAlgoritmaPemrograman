package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.UserModel;
import javax.swing.JPasswordField;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;

public class UserProfileView extends JFrame {
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtPhoneNumber;
    private JLabel lblPhoto;
    private JButton btnUploadPhoto;
    private JButton btnUpdate;
    private JButton btnBack;
    private JButton btnDeleteAccount; // Tombol baru untuk hapus akun
    private String selectedPhotoPath;
    private JButton btnChangePassword;
    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private UserModel currentUser;

    public UserProfileView() {
        setTitle("User Profile");
        setSize(500, 800); // Ukuran jendela ditambah agar dapat memuat tombol baru
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Panel konten
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Foto profil
        lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(150, 150));
        lblPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);

        btnUploadPhoto = new JButton("Upload Photo");
        styleButton(btnUploadPhoto);

        // Fields
        txtUsername = createStyledTextField();
        txtEmail = createStyledTextField();
        txtAddress = createStyledTextField();
        txtPhoneNumber = createStyledTextField();

        btnUpdate = new JButton("Update Profile");
        styleButton(btnUpdate);

        //tombol change pw
        btnChangePassword = new JButton("Change Password");
        styleButton(btnChangePassword);

        //btn back
        btnBack = new JButton("Back");
        styleButton(btnBack);

        //btn hapus akun
        btnDeleteAccount = new JButton("Delete Account"); // Tombol untuk hapus akun
        styleButton(btnDeleteAccount); // Menambahkan gaya tombol

        // Menambahkan komponen
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(lblPhoto, gbc);

        gbc.gridy = 1;
        contentPanel.add(btnUploadPhoto, gbc);

        addFormField(contentPanel, "Username:", txtUsername, gbc, 2);
        addFormField(contentPanel, "Email:", txtEmail, gbc, 4);
        addFormField(contentPanel, "Address:", txtAddress, gbc, 6);
        addFormField(contentPanel, "Phone Number:", txtPhoneNumber, gbc, 8);

        gbc.gridy = 10;
        gbc.gridwidth = 2;
        contentPanel.add(btnUpdate, gbc);

        gbc.gridy = 11;
        contentPanel.add(btnChangePassword, gbc);

        gbc.gridy = 12;
        contentPanel.add(btnBack, gbc);

        gbc.gridy = 13;
        contentPanel.add(btnDeleteAccount, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return field;
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(300, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    private void addFormField(JPanel panel, String label, JComponent field,
                              GridBagConstraints gbc, int gridy) {
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridy = gridy + 1;
        gbc.gridwidth = 2;
        panel.add(field, gbc);
    }

    // Setter untuk data user
    public void setUserData(UserModel user) {
        System.out.println("DEBUG: Setting user data in UserProfileView:");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Photo Path: " + user.getPhotoPath());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Phone Number: " + user.getPhoneNumber());

        // Implementasi pengaturan data ke text fields dan label (cek jika null)
        txtUsername.setText(user.getUsername() != null ? user.getUsername() : "");
        txtEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        txtAddress.setText(user.getAddress() != null ? user.getAddress() : "");
        txtPhoneNumber.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "No Phone Number");

        // Logika untuk foto pengguna
        if (user.getPhotoPath() != null && !user.getPhotoPath().isEmpty()) {
            File photoFile = new File(user.getPhotoPath());
            if (photoFile.exists()) {
                ImageIcon imageIcon = new ImageIcon(photoFile.getAbsolutePath());
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblPhoto.setIcon(new ImageIcon(scaledImage));
            } else {
                lblPhoto.setIcon(null);
                lblPhoto.setText("Photo not found");
            }
        } else {
            lblPhoto.setIcon(null);
            lblPhoto.setText("No photo");
        }
    }

    private void displayPhoto(String path) {
        try {
            ImageIcon imageIcon = new ImageIcon(path);
            Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblPhoto.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            lblPhoto.setIcon(null);
            lblPhoto.setText("No Photo");
        }
    }

    // Getters untuk data form
    public String getUsername() { return txtUsername.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getAddress() { return txtAddress.getText(); }
    public String getPhoneNumber() { return txtPhoneNumber.getText(); }
    public String getSelectedPhotoPath() { return selectedPhotoPath; }

    // Listeners
    public void addUpdateListener(ActionListener listener) {
        btnUpdate.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        btnBack.addActionListener(listener);
    }

    public void addUploadPhotoListener(ActionListener listener) {
        btnUploadPhoto.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedPhotoPath = fileChooser.getSelectedFile().getAbsolutePath();
                displayPhoto(selectedPhotoPath);
                listener.actionPerformed(e);
            }
        });
    }

    // Menambahkan listener untuk tombol hapus akun
    public void addDeleteAccountListener(ActionListener listener) {
        btnDeleteAccount.addActionListener(e -> {
            // Menampilkan konfirmasi untuk menghapus akun
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete your account?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            // Jika pengguna memilih "Yes", panggil listener
            if (confirm == JOptionPane.YES_OPTION) {
                listener.actionPerformed(e);
            }
        });
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }


    //method rubah pw
    private void showChangePasswordDialog() {
        JDialog dialog = new JDialog(this, "Change Password", true);
        dialog.setSize(350, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        txtCurrentPassword = new JPasswordField(20);
        txtNewPassword = new JPasswordField(20);
        txtConfirmPassword = new JPasswordField(20);

        // Styling password fields
        stylePasswordField(txtCurrentPassword);
        stylePasswordField(txtNewPassword);
        stylePasswordField(txtConfirmPassword);

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Current Password:"), gbc);
        gbc.gridy = 1;
        panel.add(txtCurrentPassword, gbc);

        gbc.gridy = 2;
        panel.add(new JLabel("New Password:"), gbc);
        gbc.gridy = 3;
        panel.add(txtNewPassword, gbc);

        gbc.gridy = 4;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridy = 5;
        panel.add(txtConfirmPassword, gbc);

        JButton btnSubmit = new JButton("Change Password");
        styleButton(btnSubmit);
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(btnSubmit, gbc);

        dialog.add(panel);

        btnSubmit.addActionListener(e -> {
            if (validatePasswordChange()) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private void stylePasswordField(JPasswordField field) {
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    private boolean validatePasswordChange() {
        String currentPass = new String(txtCurrentPassword.getPassword());
        String newPass = new String(txtNewPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showMessage("All fields are required!");
            return false;
        }

        if (!newPass.equals(confirmPass)) {
            showMessage("New password and confirm password do not match!");
            return false;
        }

        if (newPass.length() < 6) {
            showMessage("New password must be at least 6 characters long!");
            return false;
        }

        return true;
    }

    // Tambahkan getter untuk password
    public String getCurrentPassword() {
        return new String(txtCurrentPassword.getPassword());
    }

    public String getNewPassword() {
        return new String(txtNewPassword.getPassword());
    }

    // Tambahkan method untuk listener change password
    public void addChangePasswordListener(ActionListener listener) {
        btnChangePassword.addActionListener(e -> {
            showChangePasswordDialog();
            if (validatePasswordChange()) {
                String currentPassword = new String(txtCurrentPassword.getPassword());
                String newPassword = new String(txtNewPassword.getPassword());
                listener.actionPerformed(new ActionEvent(
                        this,
                        ActionEvent.ACTION_PERFORMED,
                        currentPassword + "," + newPassword
                ));
            }
        });
    }
}


