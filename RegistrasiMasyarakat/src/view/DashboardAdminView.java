package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import util.PDFGenerator;

public class DashboardAdminView extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable kategoriTable;
    private JTable jenisTable;
    private JTable userTable;
    private DefaultTableModel kategoriTableModel;
    private DefaultTableModel jenisTableModel;
    private DefaultTableModel userTableModel;

    // Buttons for Kategori
    private JButton addKategoriButton;
    private JButton editKategoriButton;
    private JButton deleteKategoriButton;
    private JButton refreshKategoriButton;
    private JButton exportKategoriPdfButton;

    // Buttons for Jenis
    private JButton addJenisButton;
    private JButton editJenisButton;
    private JButton deleteJenisButton;
    private JButton refreshJenisButton;
    private JButton exportJenisPdfButton;

    // Buttons for Users
    private JButton editUserButton;
    private JButton deleteUserButton;
    private JButton refreshUserButton;
    private JButton exportUserPdfButton;

    // Logout button
    private JButton logoutButton;

    public DashboardAdminView() {
        setTitle("Dashboard Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Initialize components
        initializeComponents();

        // Layout setup
        setupLayout();

        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        // Initialize table models
        kategoriTableModel = new DefaultTableModel(
                new String[]{"ID", "Nama Kategori"}, 0
        );
        jenisTableModel = new DefaultTableModel(
                new String[]{"ID", "Nama Jenis", "Kategori"}, 0
        );
        userTableModel = new DefaultTableModel(
                new String[]{"ID", "Username", "Email", "Phone Number", "Address", "Role"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Create tables
        kategoriTable = new JTable(kategoriTableModel);
        jenisTable = new JTable(jenisTableModel);
        userTable = new JTable(userTableModel);

        // Initialize buttons
        addKategoriButton = new JButton("Tambah");
        editKategoriButton = new JButton("Edit");
        deleteKategoriButton = new JButton("Hapus");
        refreshKategoriButton = new JButton("Refresh");
        exportKategoriPdfButton = new JButton("Export Pdf");

        addJenisButton = new JButton("Tambah");
        editJenisButton = new JButton("Edit");
        deleteJenisButton = new JButton("Hapus");
        refreshJenisButton = new JButton("Refresh");
        exportJenisPdfButton = new JButton("Export Pdf");

        editUserButton = new JButton("Edit");
        deleteUserButton = new JButton("Hapus");
        refreshUserButton = new JButton("Refresh");
        exportUserPdfButton = new JButton("Export Pdf");

        logoutButton = new JButton("Logout");
    }

    private void setupLayout() {
        // Kategori Panel
        JPanel kategoriPanel = new JPanel(new BorderLayout());
        JPanel kategoriButtonPanel = new JPanel();
        kategoriButtonPanel.add(addKategoriButton);
        kategoriButtonPanel.add(editKategoriButton);
        kategoriButtonPanel.add(deleteKategoriButton);
        kategoriButtonPanel.add(refreshKategoriButton);
        kategoriButtonPanel.add(exportKategoriPdfButton);

        kategoriPanel.add(new JScrollPane(kategoriTable), BorderLayout.CENTER);
        kategoriPanel.add(kategoriButtonPanel, BorderLayout.SOUTH);

        // Jenis Panel
        JPanel jenisPanel = new JPanel(new BorderLayout());
        JPanel jenisButtonPanel = new JPanel();
        jenisButtonPanel.add(addJenisButton);
        jenisButtonPanel.add(editJenisButton);
        jenisButtonPanel.add(deleteJenisButton);
        jenisButtonPanel.add(refreshJenisButton);
        jenisButtonPanel.add(exportJenisPdfButton);

        jenisPanel.add(new JScrollPane(jenisTable), BorderLayout.CENTER);
        jenisPanel.add(jenisButtonPanel, BorderLayout.SOUTH);

        // User management panel setup
        JPanel userPanel = new JPanel(new BorderLayout());
        JPanel userButtonPanel = new JPanel();
        userButtonPanel.add(editUserButton);
        userButtonPanel.add(deleteUserButton);
        userButtonPanel.add(refreshUserButton);
        userButtonPanel.add(exportUserPdfButton);

        userPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        userPanel.add(userButtonPanel, BorderLayout.SOUTH);

        // Add tabs
        tabbedPane.addTab("Kategori", kategoriPanel);
        tabbedPane.addTab("Jenis", jenisPanel);
        tabbedPane.addTab("Users", userPanel);

        // Main layout
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        // Add logout button to top
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);
    }

    // Getter methods for tables and models
    public JTable getKategoriTable() { return kategoriTable; }
    public JTable getJenisTable() { return jenisTable; }
    public DefaultTableModel getKategoriTableModel() { return kategoriTableModel; }
    public DefaultTableModel getJenisTableModel() { return jenisTableModel; }
    public JTable getUserTable() { return userTable; }
    public DefaultTableModel getUserTableModel() { return userTableModel; }

    // Button listener methods for Kategori
    public void addKategoriAddListener(ActionListener listener) {
        addKategoriButton.addActionListener(listener);
    }

    public void addKategoriEditListener(ActionListener listener) {
        editKategoriButton.addActionListener(listener);
    }

    public void addKategoriDeleteListener(ActionListener listener) {
        deleteKategoriButton.addActionListener(listener);
    }

    public void addKategoriRefreshListener(ActionListener listener) {
        refreshKategoriButton.addActionListener(listener);
    }

    // Button listener methods for Jenis
    public void addJenisAddListener(ActionListener listener) {
        addJenisButton.addActionListener(listener);
    }

    public void addJenisEditListener(ActionListener listener) {
        editJenisButton.addActionListener(listener);
    }

    public void addJenisDeleteListener(ActionListener listener) {
        deleteJenisButton.addActionListener(listener);
    }

    public void addJenisRefreshListener(ActionListener listener) {
        refreshJenisButton.addActionListener(listener);
    }

    // User management button listeners
    public void addUserEditListener(ActionListener listener) {
        editUserButton.addActionListener(listener);
    }

    public void addUserDeleteListener(ActionListener listener) {
        deleteUserButton.addActionListener(listener);
    }

    public void addUserRefreshListener(ActionListener listener) {
        refreshUserButton.addActionListener(listener);
    }

    // Logout listener
    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void addUserPdfExportListener(ActionListener listener) {
        exportUserPdfButton.addActionListener(listener);
    }

    public void addJenisPdfExportListener(ActionListener listener) {
        exportJenisPdfButton.addActionListener(listener);
    }

    public void addKategoriPdfExportListener(ActionListener listener) {
        exportKategoriPdfButton.addActionListener(listener);
    }
}