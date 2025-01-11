package view;

import model.UserModel;
import javax.swing.*;
import java.awt.*;

public class FormUser extends JDialog {
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JComboBox<String> roleComboBox;
    private boolean dataChanged = false;

    public FormUser(JFrame parent, UserModel user) {
        super(parent, "Edit User", true);
        setupComponents(user);
        setupLayout();
        pack();
        setLocationRelativeTo(parent);
    }

    private void setupComponents(UserModel user) {
        usernameField = new JTextField(user.getUsername(), 20);
        emailField = new JTextField(user.getEmail(), 20);
        phoneField = new JTextField(user.getPhoneNumber(), 20);
        addressField = new JTextField(user.getAddress(), 20);

        roleComboBox = new JComboBox<>(new String[]{"Admin", "User"});
        roleComboBox.setSelectedItem(user.getRoleId() == 1 ? "Admin" : "User");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            dataChanged = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getUsername() { return usernameField.getText().trim(); }
    public String getEmail() { return emailField.getText().trim(); }
    public String getPhoneNumber() { return phoneField.getText().trim(); }
    public String getAddress() { return addressField.getText().trim(); }
    public int getRoleId() { return "Admin".equals(roleComboBox.getSelectedItem()) ? 1 : 2; }
    public boolean isDataChanged() { return dataChanged; }
}
