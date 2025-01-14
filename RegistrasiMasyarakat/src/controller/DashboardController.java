package controller;

import model.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import util.PDFGenerator;
import util.SessionManager;
import view.DashboardAdminView;
import view.FormJenis;
import view.FormKategori;
import view.FormUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.List;

public class DashboardController {
    private final DashboardAdminView dashboardView;
    private final SqlSessionFactory sessionFactory;

    public DashboardController(SqlSessionFactory sessionFactory) {
        this.dashboardView = new DashboardAdminView();
        this.sessionFactory = sessionFactory;

        setupListeners();
        loadData();
    }

    public void showDashboard() {
        dashboardView.setVisible(true);
    }

    private void setupListeners() {
        // Listener untuk kategori
        dashboardView.addKategoriAddListener(e -> showAddKategoriDialog());
        dashboardView.addKategoriEditListener(e -> showEditKategoriDialog());
        dashboardView.addKategoriDeleteListener(e -> deleteKategori());
        dashboardView.addKategoriRefreshListener(e -> loadKategoriData());
        dashboardView.addKategoriPdfExportListener(e -> generateKategoriPdf());

        // Listener untuk jenis
        dashboardView.addJenisAddListener(e -> showAddJenisDialog());
        dashboardView.addJenisEditListener(e -> showEditJenisDialog());
        dashboardView.addJenisDeleteListener(e -> deleteJenis());
        dashboardView.addJenisRefreshListener(e -> loadJenisData());
        dashboardView.addJenisPdfExportListener(e -> generateJenisPdf());

        // User management listeners
        dashboardView.addUserEditListener(e -> showEditUserDialog());
        dashboardView.addUserDeleteListener(e -> deleteUser());
        dashboardView.addUserRefreshListener(e -> loadUserData());
        dashboardView.addUserPdfExportListener(e -> generateUserPdf());

        // Listener untuk logout
        dashboardView.addLogoutListener(e -> handleLogout());
    }

    private void loadData() {
        loadKategoriData();
        loadJenisData();
        loadUserData();
    }

    private void loadKategoriData() {
        try (SqlSession session = sessionFactory.openSession()) {
            KategoriMapper kategoriMapper = session.getMapper(KategoriMapper.class);
            List<KategoriModel> kategoris = kategoriMapper.getAllKategori();

            DefaultTableModel model = dashboardView.getKategoriTableModel();
            model.setRowCount(0);

            for (KategoriModel kategori : kategoris) {
                model.addRow(new Object[]{kategori.getId(), kategori.getNamaKategori()});
            }
        }
    }

    private void loadJenisData() {
        try (SqlSession session = sessionFactory.openSession()) {
            JenisMapper jenisMapper = session.getMapper(JenisMapper.class);
            List<JenisModel> jenisList = jenisMapper.getAllJenis();

            DefaultTableModel model = dashboardView.getJenisTableModel();
            model.setRowCount(0);

            for (JenisModel jenis : jenisList) {
                model.addRow(new Object[]{jenis.getId(), jenis.getNamaJenis(), jenis.getNamaKategori()});
            }
        }
    }

    private void loadUserData() {
        try (SqlSession session = sessionFactory.openSession()) {
            if (sessionFactory == null) {
                throw new IllegalStateException("SessionFactory is not initialized.");
            }

            UserMapper userMapper = session.getMapper(UserMapper.class);
            List<UserModel> users = userMapper.getAllUsers();

            DefaultTableModel model = dashboardView.getUserTableModel();
            model.setRowCount(0);

            for (UserModel user : users) {
                model.addRow(new Object[]{
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getAddress(),
                        user.getRoleId() == 1 ? "Admin" : "User"
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Log full stack trace for debugging
            dashboardView.showMessage("Error loading user data: " + ex.getMessage());
        }
    }

    private void showAddKategoriDialog() {
        FormKategori dialog = new FormKategori(dashboardView, "Tambah Kategori");

        dialog.addSaveButtonListener(e -> {
            String nama = dialog.getNamaKategori();
            if (!nama.isEmpty()) {
                try (SqlSession session = sessionFactory.openSession()) {
                    KategoriMapper kategoriMapper = session.getMapper(KategoriMapper.class);
                    KategoriModel newKategori = new KategoriModel();
                    newKategori.setNamaKategori(nama);
                    kategoriMapper.insert(newKategori);
                    session.commit();
                    loadKategoriData();
                    dialog.dispose();
                }
            } else {
                dashboardView.showMessage("Nama kategori tidak boleh kosong!");
            }
        });

        dialog.setVisible(true);
    }

    private void showEditKategoriDialog() {
        int selectedRow = dashboardView.getKategoriTable().getSelectedRow();
        if (selectedRow < 0) {
            dashboardView.showMessage("Pilih kategori yang akan diedit!");
            return;
        }

        try (SqlSession session = sessionFactory.openSession()) {
            KategoriMapper kategoriMapper = session.getMapper(KategoriMapper.class);

            // Get ID from selected row
            int id = (int) dashboardView.getKategoriTableModel().getValueAt(selectedRow, 0);
            KategoriModel kategori = kategoriMapper.getKategoriById(id);

            if (kategori == null) {
                dashboardView.showMessage("Kategori tidak ditemukan!");
                return;
            }

            FormKategori dialog = new FormKategori(dashboardView, "Edit Kategori");
            dialog.setNamaKategori(kategori.getNamaKategori());

            dialog.addSaveButtonListener(e -> {
                String newNama = dialog.getNamaKategori().trim();

                if (newNama.isEmpty()) {
                    dashboardView.showMessage("Nama kategori tidak boleh kosong!");
                    return;
                }

                try {
                    kategori.setNamaKategori(newNama);
                    kategoriMapper.update(kategori);
                    session.commit();
                    loadKategoriData();
                    dialog.dispose();
                    dashboardView.showMessage("Kategori berhasil diperbarui!");
                } catch (Exception ex) {
                    session.rollback();
                    dashboardView.showMessage("Gagal mengupdate kategori: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            dialog.setVisible(true);
        } catch (Exception ex) {
            dashboardView.showMessage("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showAddJenisDialog() {
        try (SqlSession session = sessionFactory.openSession()) {
            KategoriMapper kategoriMapper = session.getMapper(KategoriMapper.class);
            List<KategoriModel> kategoris = kategoriMapper.getAllKategori();
            FormJenis dialog = new FormJenis(dashboardView, "Tambah Jenis", kategoris);

            dialog.addSaveButtonListener(e -> {
                String nama = dialog.getNamaJenis();
                KategoriModel selectedKategori = dialog.getSelectedKategori();

                if (!nama.isEmpty() && selectedKategori != null) {
                    try (SqlSession saveSession = sessionFactory.openSession()) {
                        JenisMapper jenisMapper = saveSession.getMapper(JenisMapper.class);
                        JenisModel newJenis = new JenisModel();
                        newJenis.setNamaJenis(nama);
                        newJenis.setKategoriId(selectedKategori.getId());

                        jenisMapper.insertJenis(newJenis); // Gunakan objek model
                        saveSession.commit();
                        loadJenisData();
                        dialog.dispose();
                    } catch (Exception ex) {
                        dashboardView.showMessage("Gagal menambah jenis: " + ex.getMessage());
                    }
                } else {
                    dashboardView.showMessage("Semua field harus diisi!");
                }
            });
            dialog.setVisible(true);
        } catch (Exception ex) {
            dashboardView.showMessage("Gagal memuat data kategori: " + ex.getMessage());
        }
    }

    private void showEditJenisDialog() {
        int selectedRow = dashboardView.getJenisTable().getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) dashboardView.getJenisTableModel().getValueAt(selectedRow, 0);
            try (SqlSession session = sessionFactory.openSession()) {
                JenisMapper jenisMapper = session.getMapper(JenisMapper.class);
                JenisModel jenis = jenisMapper.getJenisById(id);
                KategoriMapper kategoriMapper = session.getMapper(KategoriMapper.class);
                List<KategoriModel> kategoris = kategoriMapper.getAllKategori();

                if (jenis != null) {
                    FormJenis dialog = new FormJenis(dashboardView, "Edit Jenis", kategoris);
                    dialog.setNamaJenis(jenis.getNamaJenis());

                    for (KategoriModel kategori : kategoris) {
                        if (kategori.getId() == jenis.getKategoriId()) {
                            dialog.setSelectedKategori(kategori);
                            break;
                        }
                    }

                    dialog.addSaveButtonListener(e -> {
                        String newNama = dialog.getNamaJenis();
                        KategoriModel newKategori = dialog.getSelectedKategori();

                        if (!newNama.isEmpty() && newKategori != null) {
                            try (SqlSession saveSession = sessionFactory.openSession()) {
                                JenisMapper saveJenisMapper = saveSession.getMapper(JenisMapper.class);
                                jenis.setNamaJenis(newNama);
                                jenis.setKategoriId(newKategori.getId());

                                saveJenisMapper.updateJenis(jenis.getId(), newNama, newKategori.getId()); // Gunakan ID
                                saveSession.commit();
                                loadJenisData();
                                dialog.dispose();
                            } catch (Exception ex) {
                                dashboardView.showMessage("Gagal mengedit jenis: " + ex.getMessage());
                            }
                        } else {
                            dashboardView.showMessage("Semua field harus diisi!");
                        }
                    });

                    dialog.setVisible(true);
                } else {
                    dashboardView.showMessage("Jenis yang dipilih tidak ditemukan!");
                }
            } catch (Exception ex) {
                dashboardView.showMessage("Gagal memuat data jenis: " + ex.getMessage());
            }
        } else {
            dashboardView.showMessage("Pilih jenis yang akan diedit!");
        }
    }

    private void showEditUserDialog() {
        int selectedRow = dashboardView.getUserTable().getSelectedRow();
        if (selectedRow < 0) {
            dashboardView.showMessage("Please select a user to edit!");
            return;
        }

        try (SqlSession session = sessionFactory.openSession()) {
            if (sessionFactory == null) {
                throw new IllegalStateException("SessionFactory is not initialized.");
            }

            UserMapper userMapper = session.getMapper(UserMapper.class);
            int userId = (int) dashboardView.getUserTableModel().getValueAt(selectedRow, 0);
            UserModel user = userMapper.findById(userId);

            if (user != null) {
                // Create and show edit dialog
                FormUser dialog = new FormUser(dashboardView, user);
                dialog.setVisible(true);

                if (dialog.isDataChanged()) {
                    // Update user data
                    user.setUsername(dialog.getUsername());
                    user.setEmail(dialog.getEmail());
                    user.setPhoneNumber(dialog.getPhoneNumber());
                    user.setAddress(dialog.getAddress());
                    user.setRoleId(dialog.getRoleId());

                    userMapper.updateProfile(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getPhotoPath(),
                            user.getAddress(),
                            user.getPhoneNumber()
                    );
                    session.commit();
                    loadUserData();
                }
            } else {
                dashboardView.showMessage("User not found!");
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Log full stack trace for debugging
            dashboardView.showMessage("Error editing user: " + ex.getMessage());
        }
    }

    private void deleteUser() {
        int selectedRow = dashboardView.getUserTable().getSelectedRow();
        if (selectedRow < 0) {
            dashboardView.showMessage("Please select a user to delete!");
            return;
        }

        int userId = (int) dashboardView.getUserTableModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                dashboardView,
                "Are you sure you want to delete this user?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try (SqlSession session = sessionFactory.openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                userMapper.deleteUser(userId);
                session.commit();
                loadUserData();
                dashboardView.showMessage("User deleted successfully!");
            } catch (Exception ex) {
                dashboardView.showMessage("Error deleting user: " + ex.getMessage());
            }
        }
    }



    private void deleteKategori() {
        int selectedRow = dashboardView.getKategoriTable().getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) dashboardView.getKategoriTableModel().getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    dashboardView,
                    "Apakah anda yakin ingin menghapus kategori ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try (SqlSession session = sessionFactory.openSession()) {
                    KategoriMapper kategoriMapper = session.getMapper(KategoriMapper.class);
                    kategoriMapper.delete(id);
                    session.commit();
                    loadKategoriData();
                }
            }
        } else {
            dashboardView.showMessage("Pilih kategori yang akan dihapus!");
        }
    }

    private void generateUserPdf() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF Report");
            fileChooser.setSelectedFile(new File("user_report.pdf"));

            if (fileChooser.showSaveDialog(dashboardView) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }
                PDFGenerator.generateUserReport(dashboardView.getUserTableModel(), filePath);
                dashboardView.showMessage("PDF report generated successfully!");
            }
        } catch (Exception ex) {
            dashboardView.showMessage("Error generating PDF: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void generateJenisPdf() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF Report");
            fileChooser.setSelectedFile(new File("jenis_report.pdf"));

            if (fileChooser.showSaveDialog(dashboardView) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }
                PDFGenerator.generateJenisReport(dashboardView.getJenisTableModel(), filePath);
                dashboardView.showMessage("PDF report generated successfully!");
            }
        } catch (Exception ex) {
            dashboardView.showMessage("Error generating PDF: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void generateKategoriPdf() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF Report");
            fileChooser.setSelectedFile(new File("kategori_report.pdf"));

            if (fileChooser.showSaveDialog(dashboardView) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }
                PDFGenerator.generateKategoriReport(dashboardView.getKategoriTableModel(), filePath);
                dashboardView.showMessage("PDF report generated successfully!");
            }
        } catch (Exception ex) {
            dashboardView.showMessage("Error generating PDF: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    private void deleteJenis() {
        int selectedRow = dashboardView.getJenisTable().getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) dashboardView.getJenisTableModel().getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    dashboardView,
                    "Apakah anda yakin ingin menghapus jenis ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try (SqlSession session = sessionFactory.openSession()) {
                    JenisMapper jenisMapper = session.getMapper(JenisMapper.class);
                    jenisMapper.deleteJenis(id);
                    session.commit();
                    loadJenisData();
                }
            }
        } else {
            dashboardView.showMessage("Pilih jenis yang akan dihapus!");
        }
    }

    private void handleLogout() {
        SessionManager.getInstance().setToken(null);
        dashboardView.showMessage("Logout berhasil!");
        dashboardView.dispose();
    }
}
