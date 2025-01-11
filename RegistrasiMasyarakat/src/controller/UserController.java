    package controller;

    import java.io.File;
    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import model.*;
    import org.apache.ibatis.session.SqlSessionFactory;
    import util.MyBatisUtil;
    import view.*;
    import util.*;
    import org.apache.ibatis.session.SqlSession;
    import javax.mail.MessagingException;
    import javax.swing.*;
    import java.sql.Timestamp;
    import util.UtilityHelper;

    public class UserController {
        private UserModel model;
        private HalamanAwalView halamanAwalView;
        private LoginView loginView;
        private RegisterView registerView;
        private KategoriSampahView kategoriSampahView;
        private JenisSampahView jenisSampahView;
        private String currentOTP;
        private long otpGenerationTime;
        private HalamanUtamaView halamanUtamaView;
        private static final long OTP_EXPIRY_TIME = 15 * 60 * 1000;
        private static final long SESSION_EXPIRY_TIME = 24 * 60 * 60 * 1000;
        private ForgotPasswordView forgotPasswordView;
        private String resetCode;
        private String resetEmail;
        private UserProfileView userProfileView;
        private UserModel loggedInUser;
        private static final String UPLOAD_DIRECTORY = "Kelompok7_PraktikumPemrograman/RegistrasiMasyarakat/src/upload/profile_photos";
        private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
        private boolean validateRegistrationInput(String username, String password, String email, String phoneNumber) {
            // Validasi username
            if (username == null || username.trim().isEmpty()) {
                registerView.showMessage("Username tidak boleh kosong!");
                return false;
            }

            // Validasi password
            if (password == null || password.length() < 6) {
                registerView.showMessage("Password harus minimal 6 karakter!");
                return false;
            }

            // Validasi email
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                registerView.showMessage("Email tidak valid!");
                return false;
            }

            // Validasi nomor telepon
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                registerView.showMessage("Nomor telepon tidak boleh kosong!");
                return false;
            }

            return true; // Semua validasi berhasil
        }

        public UserController(UserModel userModel, HalamanAwalView halamanAwalView) {
            this.model = userModel;
            this.halamanAwalView = halamanAwalView;
            this.loginView = new LoginView();
            this.registerView = new RegisterView();
            this.forgotPasswordView = new ForgotPasswordView();
            this.halamanUtamaView = new HalamanUtamaView();

            this.loginView.addLoginListener(e -> login());
            this.loginView.addRegisterNavigationListener(e -> showRegister());
            this.loginView.addForgotPasswordListener(e -> showForgotPassword());

            this.registerView.addRegisterListener(e -> register());
            this.registerView.addLoginNavigationListener(e -> showLogin());

            this.forgotPasswordView.addSendCodeListener(e -> handleForgotPassword());
            this.forgotPasswordView.addBackToLoginListener(e -> showLogin());

            setupHalamanUtamaListeners();
            setupHalamanAwalListeners();
        }

        public void showHalamanAwal() {
            halamanAwalView.setVisible(true);
            loginView.setVisible(false);
            registerView.setVisible(false);
        }
        private void setupHalamanAwalListeners() {
            halamanAwalView.addLoginButtonListener(e -> {
                halamanAwalView.setVisible(false);
                showLogin();
            });

            halamanAwalView.addRegisterButtonListener(e -> {
                halamanAwalView.setVisible(false);
                showRegister();
            });
        }

        private void setupHalamanUtamaListeners() {
            halamanUtamaView.addProfileButtonListener(e -> {
                if (loggedInUser != null) {
                    halamanUtamaView.dispose();
                    showUserProfile(loggedInUser);
                } else {
                    halamanUtamaView.showMessage("Silakan login terlebih dahulu!");
                    showLogin();
                }
            });

            halamanUtamaView.addKategoriSampahButtonListener(e -> {
                halamanUtamaView.dispose();
                showKategoriSampah();
            });

            halamanUtamaView.addJenisSampahButtonListener(e -> {
                halamanUtamaView.dispose();
                showJenisSampah();
            });

            halamanUtamaView.addLogoutButtonListener(e -> {
                SessionManager.getInstance().setToken(null);
                loggedInUser = null;
                halamanUtamaView.dispose();
                showHalamanAwal();
                halamanUtamaView.showMessage("Logout berhasil!");
            });
        }

        private void login() {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String hashedPassword = UtilityHelper.hashPassword(password);

            try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                SessionMapper sessionMapper = session.getMapper(SessionMapper.class);

                // Periksa username dan password
                UserModel user = userMapper.findByUsernameAndPassword(username, hashedPassword);

                if (user != null) {
                    // Generate token sesi
                    String token = JWTUtil.generateToken(user.getId());
                    Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + SESSION_EXPIRY_TIME);

                    // Simpan sesi
                    sessionMapper.insert(user.getId(), token, expiresAt);
                    session.commit();

                    // Set user yang sedang login
                    this.loggedInUser = user;
                    SessionManager.getInstance().setToken(token);

                    loginView.showMessage("Login berhasil!");

                    // Arahkan ke halaman berdasarkan role
                    if (user.getRoleId() == 1) { // Misal role_id = 1 untuk admin
                        openHalamanDashboardAdmin();
                    } else {
                        openHalamanUtamaView();
                    }
                } else {
                    loginView.showMessage("Username atau password salah!");
                }
            } catch (Exception ex) {
                loginView.showMessage("Error: " + ex.getMessage());
            }
        }


        public void openHalamanDashboardAdmin() {
            SqlSessionFactory factory = MyBatisUtil.getSqlSessionFactory();
            DashboardController dashboardController = new DashboardController(factory);
            dashboardController.showDashboard();
        }


        private void register() {
            String username = registerView.getUsername();
            String password = registerView.getPassword();
            String email = registerView.getEmail();
            String phoneNumber = registerView.getPhoneNumber();

            if (!validateRegistrationInput(username, password, email, phoneNumber)) {
                return;
            }

            try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                OTPVerificationMapper otpMapper = session.getMapper(OTPVerificationMapper.class);

                // Check if username or email already exists
                if (userMapper.findByUsername(username) != null) {
                    registerView.showMessage("Username sudah digunakan!");
                    return;
                }
                if (userMapper.findByEmail(email) != null) {
                    registerView.showMessage("Email sudah terdaftar!");
                    return;
                }

                // Generate OTP
                String otpCode = OTPUtil.generateOTP();
                Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + OTP_EXPIRY_TIME);

                System.out.println("Debug - Email: " + email);
                System.out.println("Debug - OTP Code: " + otpCode);
                System.out.println("Debug - Expires At: " + expiresAt);

                try {
                    // Delete existing unverified OTPs for this email
                    otpMapper.deleteByEmail(email);

                    // Insert new OTP verification
                    int insertResult = otpMapper.insert(email, otpCode, expiresAt);
                    System.out.println("Debug - Insert Result: " + insertResult);

                    if (insertResult > 0) {
                        session.commit();

                        OTPVerification savedOTP = otpMapper.findByEmail(email);
                        System.out.println("Debug - Saved OTP: " + (savedOTP != null ? savedOTP.getOtpCode() : "null"));

                        // Send OTP email
                        OTPUtil.sendOTPEmail(email, otpCode);
                        registerView.showMessage("Kode verifikasi telah dikirim ke email Anda.");

                        // Show verification dialog
                        TokenController tokenController = new TokenController();
                        showVerificationDialog(email, tokenController, session, userMapper, username, password, phoneNumber);
                    } else {
                        session.rollback();
                        registerView.showMessage("Gagal menyimpan data verifikasi!");
                    }
                } catch (MessagingException ex) {
                    session.rollback();
                    registerView.showMessage("Gagal mengirim email verifikasi: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                registerView.showMessage("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }



    private void showVerificationDialog(String email, TokenController tokenController, SqlSession session,
                                        UserMapper userMapper, String username, String password, String phoneNumber) {
        OTPDialog otpDialog = new OTPDialog(registerView);

        otpDialog.addVerifyListener(e -> {
            String otpInput = otpDialog.getOTP().trim();

            if (otpInput.isEmpty()) {
                registerView.showMessage("Kode OTP tidak boleh kosong!");
                return;
            }

            boolean isVerified = tokenController.verifyOTP(email, otpInput);

            if (isVerified) {
                try {
                    // Hash the password
                    String hashedPassword = UtilityHelper.hashPassword(password);

                    // Save user to database
                    int insertResult = userMapper.insertWithVerification(
                            username, hashedPassword, email, phoneNumber
                    );


                    if (insertResult > 0) {
                        session.commit();
                        otpDialog.dispose();
                        registerView.showMessage("Registrasi berhasil! Silakan login.");
                        showLogin();
                    } else {
                        session.rollback();
                        registerView.showMessage("Gagal menyimpan data pengguna!");
                    }
                } catch (Exception ex) {
                    session.rollback();
                    registerView.showMessage("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                registerView.showMessage("Kode OTP salah atau sudah kadaluarsa!");
            }
        });

        otpDialog.setVisible(true);
    }

    private void showForgotPassword() {
        loginView.setVisible(false);
        forgotPasswordView.setVisible(true);
    }

    public void showLogin() {
        loginView.setVisible(true);
        registerView.setVisible(false);
    }

    public void showRegister() {
        registerView.setVisible(true);
        loginView.setVisible(false);
    }

    public void showKategoriSampah() {
        if (kategoriSampahView != null) {
            kategoriSampahView.dispose();
        }
        SqlSessionFactory factory = MyBatisUtil.getSqlSessionFactory();
        KategoriSampahController kategoriController = new KategoriSampahController(factory);
        kategoriController.showKategoriSampah();
    }

    public void showJenisSampah() {
        if (jenisSampahView != null) {
            jenisSampahView.dispose();
        }
        SqlSessionFactory factory = MyBatisUtil.getSqlSessionFactory();
        JenisSampahController jenisController = new JenisSampahController(factory);
        jenisController.showJenisSampah();
    }

    private void handleForgotPassword() {
            String email = forgotPasswordView.getEmail();

            if (!UtilityHelper.isValidEmail(email)) {
                forgotPasswordView.showMessage("Please enter a valid email address!");
                return;
            }

            try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                UserModel user = mapper.findByEmail(email);

                if (user != null) {
                    // Generate reset code
                    resetCode = OTPUtil.generateOTP();
                    resetEmail = email;

                    // Send reset code via email
                    try {
                        OTPUtil.sendOTPEmail(email, resetCode);
                        showResetPasswordDialog();
                    } catch (MessagingException e) {
                        forgotPasswordView.showMessage("Error sending reset code: " + e.getMessage());
                    }
                } else {
                    forgotPasswordView.showMessage("Email not found!");
                }
            } catch (Exception ex) {
                forgotPasswordView.showMessage("Error: " + ex.getMessage());
            }
        }

    private void showResetPasswordDialog() {
            ResetPasswordDialog dialog = new ResetPasswordDialog(forgotPasswordView);
            dialog.addResetListener(e -> {
                String enteredCode = dialog.getResetCode();
                String newPassword = dialog.getNewPassword();
                String confirmPassword = dialog.getConfirmPassword();

                if (!newPassword.equals(confirmPassword)) {
                    forgotPasswordView.showMessage("Passwords do not match!");
                    return;
                }

                if (!enteredCode.equals(resetCode)) {
                    forgotPasswordView.showMessage("Invalid reset code!");
                    return;
                }

                try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
                    UserMapper mapper = session.getMapper(UserMapper.class);
                    String hashedPassword = UtilityHelper.hashPassword(newPassword);

                    int result = mapper.updatePassword(resetEmail, hashedPassword);
                    session.commit();

                    if (result > 0) {
                        dialog.setResetSuccess(true);
                        dialog.dispose();
                        forgotPasswordView.showMessage("Password reset successful!");
                        showLogin();
                    } else {
                        forgotPasswordView.showMessage("Failed to reset password!");
                    }
                } catch (Exception ex) {
                    forgotPasswordView.showMessage("Error: " + ex.getMessage());
                }
            });

            dialog.setVisible(true);
        }

    public void openHalamanUtamaView() {
        if (halamanUtamaView != null) {
            halamanUtamaView.dispose();
        }

        halamanUtamaView = new HalamanUtamaView();
        setupHalamanUtamaListeners();
        halamanUtamaView.setVisible(true);
    }

    public void showUserProfile(UserModel user) {
        if (user == null) {
            System.out.println("Error: User data is null");
            return;
        }

        this.loggedInUser = user;

        // Dispose existing view if it exists
        if (userProfileView != null) {
            userProfileView.dispose();
        }

        // Create new UserProfileView instance
        userProfileView = new UserProfileView();

        // Set the user data in the view
        userProfileView.setUserData(user);

        // Add listeners for various actions
        userProfileView.addUpdateListener(e -> updateProfile());
        userProfileView.addUploadPhotoListener(e -> handlePhotoUpload());
        userProfileView.addBackListener(e -> {
            userProfileView.dispose();
            openHalamanUtamaView();
        });

        // listener untuk change password
        userProfileView.addChangePasswordListener(e -> {
            String[] passwords = e.getActionCommand().split(",");
            if (passwords.length == 2) {
                handlePasswordChange(passwords[0], passwords[1]);
            } else {
                userProfileView.showMessage("Invalid password data received!");
            }
        });

        // Display the profile view
        userProfileView.setVisible(true);
        System.out.println("UserProfileView opened for user: " + user.getUsername());

        userProfileView.addDeleteAccountListener(e -> handleAccountDeletion());
    }

    private void updateProfile() {
        // Validasi input
        if (!validateProfileInput()) {
            return;
        }
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            // Cek apakah username baru sudah digunakan (jika username diubah)
            String newUsername = userProfileView.getUsername();
            if (!newUsername.equals(loggedInUser.getUsername())) {
                UserModel existingUser = mapper.findByUsername(newUsername);
                if (existingUser != null) {
                    userProfileView.showMessage("Username sudah digunakan!");
                    return;
                }
            }

            // Cek apakah email baru sudah digunakan (jika email diubah)
            String newEmail = userProfileView.getEmail();
            if (!newEmail.equals(loggedInUser.getEmail())) {
                UserModel existingUser = mapper.findByEmail(newEmail);
                if (existingUser != null) {
                    userProfileView.showMessage("Email sudah terdaftar!");
                    return;
                }
            }

            int result = mapper.updateProfile(
                loggedInUser.getId(),
                newUsername,
                newEmail,
                loggedInUser.getPhotoPath(),
                userProfileView.getAddress(),
                userProfileView.getPhoneNumber(),
                    userProfileView.getRoleId()
            );

            session.commit();

            if (result > 0) {
                // Update model dengan data baru
                loggedInUser.setUsername(newUsername);
                loggedInUser.setEmail(newEmail);
                loggedInUser.setAddress(userProfileView.getAddress());
                loggedInUser.setPhoneNumber(userProfileView.getPhoneNumber());

                userProfileView.showMessage("Profil berhasil diperbarui!");
            } else {
                userProfileView.showMessage("Gagal memperbarui profil!");
            }
        } catch (Exception ex) {
            userProfileView.showMessage("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean validateProfileInput() {
        String username = userProfileView.getUsername();
        String email = userProfileView.getEmail();
        String address = userProfileView.getAddress();
        String phoneNumber = userProfileView.getPhoneNumber();

        if (username.trim().isEmpty() || email.trim().isEmpty() ||
            address.trim().isEmpty() || phoneNumber.trim().isEmpty()) {
            userProfileView.showMessage("Semua field harus diisi!");
            return false;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            userProfileView.showMessage("Format email tidak valid!");
            return false;
        }

        if (!phoneNumber.matches("^[0-9]{10,13}$")) {
            userProfileView.showMessage("Nomor telepon harus 10-13 digit angka!");
            return false;
        }

        return true;
    }

    private void createUploadDirectoryIfNotExists() {
        File directory = new File(UPLOAD_DIRECTORY);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Gagal membuat direktori upload!");
            }
        }
    }

    private void handlePhotoUpload() {
        String sourcePhotoPath = userProfileView.getSelectedPhotoPath();
        if (sourcePhotoPath == null || sourcePhotoPath.isEmpty()) {
            return;
        }

        // Validasi file
        File sourceFile = new File(sourcePhotoPath);
        if (!validatePhotoFile(sourceFile)) {
            return;
        }

        try {
            createUploadDirectoryIfNotExists();

            String fileExtension = sourcePhotoPath.substring(sourcePhotoPath.lastIndexOf(".")).toLowerCase();
            String newFileName = loggedInUser.getId() + "_" + System.currentTimeMillis() + fileExtension;
            Path destinationPath = Paths.get(UPLOAD_DIRECTORY, newFileName);

            // Hapus foto lama jika ada
            deleteOldPhoto();

            // Salin file baru
            Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Update database
            updatePhotoPath(UPLOAD_DIRECTORY + "/" + newFileName);

        } catch (IOException e) {
            userProfileView.showMessage("Error saat mengupload foto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validatePhotoFile(File file) {
        // Validasi ukuran
        if (file.length() > MAX_FILE_SIZE) {
            userProfileView.showMessage("Ukuran file terlalu besar! Maksimal 5MB");
            return false;
        }

        // Validasi ekstensi
        String fileName = file.getName().toLowerCase();
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") &&
            !fileName.endsWith(".png") && !fileName.endsWith(".gif")) {
            userProfileView.showMessage("Format file tidak didukung! Gunakan JPG, JPEG, PNG, atau GIF");
            return false;
        }
        return true;
    }

    private void deleteOldPhoto() {
        if (loggedInUser.getPhotoPath() != null && !loggedInUser.getPhotoPath().isEmpty()) {
            try {
                File oldPhoto = new File(loggedInUser.getPhotoPath());
                if (oldPhoto.exists()) {
                    Files.delete(oldPhoto.toPath());
                }
            } catch (IOException e) {
                System.err.println("Gagal menghapus foto lama: " + e.getMessage());
            }
        }
    }

    private void updatePhotoPath(String newPhotoPath) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            int result = mapper.updatePhotoPath(loggedInUser.getId(), newPhotoPath);
            session.commit();

            if (result > 0) {
                loggedInUser.setPhotoPath(newPhotoPath);
                userProfileView.showMessage("Foto berhasil diupload!");
            } else {
                userProfileView.showMessage("Gagal mengupdate foto di database!");
            }
        } catch (Exception ex) {
            userProfileView.showMessage("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    // rubah password
    private void handlePasswordChange(String currentPassword, String newPassword) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            // Verifikasi password lama
            String hashedCurrentPassword = UtilityHelper.hashPassword(currentPassword);
            UserModel user = mapper.findByUsernameAndPassword(loggedInUser.getUsername(), hashedCurrentPassword);

            if (user == null) {
                userProfileView.showMessage("Current password is incorrect!");
                return;
            }

            // Update password baru
            String hashedNewPassword = UtilityHelper.hashPassword(newPassword);
            int result = mapper.updatePassword(user.getEmail(), hashedNewPassword);
            session.commit();

            if (result > 0) {
                userProfileView.showMessage("Password successfully updated!");
            } else {
                userProfileView.showMessage("Failed to update password!");
            }
        } catch (Exception ex) {
            userProfileView.showMessage("Error: " + ex.getMessage());
        }
    }



    //hapus akun
    private void handleAccountDeletion() {
        if (loggedInUser == null) {
            userProfileView.showMessage("No user logged in!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                userProfileView,
                "Are you sure you want to delete your account? This action cannot be undone.",
                "Confirm Account Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                SessionMapper sessionMapper = session.getMapper(SessionMapper.class);
                OTPVerificationMapper otpMapper = session.getMapper(OTPVerificationMapper.class);

                String userEmail = loggedInUser.getEmail();
                sessionMapper.deactivateUserSessions(loggedInUser.getId());
                otpMapper.deleteByEmail(userEmail);
                int result = mapper.deleteUser(loggedInUser.getId());

                if (result > 0) {
                    session.commit();
                    SessionManager.getInstance().clearSession();
                    loggedInUser = null;

                    userProfileView.showMessage("Account successfully deleted!");
                    userProfileView.dispose();
                    showHalamanAwal();
                } else {
                    session.rollback();
                    userProfileView.showMessage("Failed to delete account!");
                }
            } catch (Exception ex) {
                userProfileView.showMessage("Error deleting account: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
