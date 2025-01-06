    package controller;

    import java.io.File;
    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import model.UserModel;
    import model.UserMapper;
    import view.LoginView;
    import view.RegisterView;
    import view.OTPDialog;
    import util.MyBatisUtil;
    import util.UtilityHelper;
    import util.OTPUtil;
    import org.apache.ibatis.session.SqlSession;
    import javax.mail.MessagingException;
    import javax.swing.SwingUtilities;
    import view.ForgotPasswordView;
    import view.HalamanUtamaView;
    import view.KategoriSampahView;
    import view.ResetPasswordDialog;
    import view.UserProfileView;

    public class UserController {
        private UserModel model;
        private LoginView loginView;
        private RegisterView registerView;
        private String currentOTP;
        private long otpGenerationTime;
        private static final long OTP_VALIDITY_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds
        private ForgotPasswordView forgotPasswordView;
        private String resetCode;
        private String resetEmail;
        private UserProfileView userProfileView;
        private UserModel loggedInUser;
        private static final String UPLOAD_DIRECTORY = "uploads/profile_photos";
        private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

        public UserController(UserModel model, LoginView loginView, RegisterView registerView) {
            this.model = model;
            this.loginView = loginView;
            this.registerView = registerView;

            // Add listeners
            this.loginView.addLoginListener(e -> login());
            this.loginView.addRegisterNavigationListener(e -> showRegister());
            this.registerView.addRegisterListener(e -> register());
            this.registerView.addLoginNavigationListener(e -> showLogin());
            this.forgotPasswordView = new ForgotPasswordView();
            this.loginView.addForgotPasswordListener(e -> showForgotPassword());
            this.forgotPasswordView.addSendCodeListener(e -> handleForgotPassword());
            this.forgotPasswordView.addBackToLoginListener(e -> showLogin());
        }

          private void login() {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String hashedPassword = UtilityHelper.hashPassword(password);

            try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                UserModel user = mapper.findByUsernameAndPassword(username, hashedPassword);

                if (user != null) {
                    // Generate and send OTP
                    currentOTP = OTPUtil.generateOTP();
                    otpGenerationTime = System.currentTimeMillis();

                    try {
                        OTPUtil.sendOTPEmail(user.getEmail(), currentOTP);
                        verifyOTP(user);
                    } catch (MessagingException e) {
                        loginView.showMessage("Error sending OTP: " + e.getMessage());
                    }
                } else {
                    loginView.showMessage("Invalid username or password!");
                }
            } catch (Exception ex) {
                loginView.showMessage("Database error: " + ex.getMessage());
            }
        }

    //    private void verifyOTP(UserModel user) {
    //        OTPDialog otpDialog = new OTPDialog(loginView);
    //        otpDialog.addVerifyListener(e -> {
    //            String enteredOTP = otpDialog.getOTP();
    //            long currentTime = System.currentTimeMillis();
    //
    //            // Check if OTP is expired
    //            if (currentTime - otpGenerationTime > OTP_VALIDITY_DURATION) {
    //                loginView.showMessage("OTP has expired! Please login again.");
    //                otpDialog.dispose();
    //                return;
    //            }
    //
    //            // Verify OTP
    //            if (enteredOTP.equals(currentOTP)) {
    //                otpDialog.setVerified(true);
    //                otpDialog.dispose();
    //                loginView.showMessage("Login successful!");
    //
    //                // Arahkan ke HalamanUtamaView
    //                HalamanUtamaView halamanUtama = new HalamanUtamaView();
    //                halamanUtama.setVisible(true);
    //                loginView.dispose(); // Menutup loginView jika diperlukan
    //            } else {
    //                loginView.showMessage("Invalid OTP! Please try again.");
    //            }
    //        });
    //
    //        otpDialog.setVisible(true);
    //
    //        // Check if verification was successful
    //        if (!otpDialog.isVerified()) {
    //            loginView.showMessage("Login cancelled or failed!");
    //        }
    //    }

        private void register() {
            String username = registerView.getUsername();
            String password = registerView.getPassword();
            String email = registerView.getEmail();

            // Validasi email
            if (!UtilityHelper.isValidEmail(email)) {
                registerView.showMessage("Invalid email format! Please enter a valid email address.");
                return;
            }

            // Validasi field kosong
            if (username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
                registerView.showMessage("All fields are required!");
                return;
            }

            // Hash password
            String hashedPassword = UtilityHelper.hashPassword(password);

            try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);

                // Cek username yang sudah ada
                if (mapper.findByUsername(username) != null) {
                    registerView.showMessage("Username already exists!");
                    return;
                }

                // Cek email yang sudah ada
                if (mapper.findByEmail(email) != null) {
                    registerView.showMessage("Email already registered!");
                    return;
                }

                // Insert user baru
                int result = mapper.insert(username, hashedPassword, email);
                session.commit(); // Jangan lupa commit untuk menyimpan perubahan

                if (result > 0) {
                    registerView.showMessage("Registration successful!");
                    showLogin();
                } else {
                    registerView.showMessage("Registration failed!");
                }
            } catch (Exception ex) {
                registerView.showMessage("Registration failed: " + ex.getMessage());
            }
        }

        private void showLogin() {
            loginView.setVisible(true);
            registerView.setVisible(false);
        }

        private void showRegister() {
            registerView.setVisible(true);
            loginView.setVisible(false);
        }

        public void start() {
            loginView.setVisible(true);
        }

        private void showForgotPassword() {
            loginView.setVisible(false);
            forgotPasswordView.setVisible(true);
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


        private void verifyOTP(UserModel user) {
        OTPDialog otpDialog = new OTPDialog(loginView);
        otpDialog.addVerifyListener(e -> {
            String enteredOTP = otpDialog.getOTP();
            long currentTime = System.currentTimeMillis();

            if (currentTime - otpGenerationTime > OTP_VALIDITY_DURATION) {
                loginView.showMessage("OTP has expired! Please login again.");
                otpDialog.dispose();
                return;
            }

            if (enteredOTP.equals(currentOTP)) {
                otpDialog.setVerified(true);
                otpDialog.dispose();
                loginView.showMessage("Login successful!");

                // Set logged in user
                this.loggedInUser = user;

                // Pastikan loginView ditutup
                loginView.setVisible(false);
                loginView.dispose();

                // Buka halaman utama menggunakan SwingUtilities.invokeLater
                SwingUtilities.invokeLater(() -> {
                    try {
                        openHalamanUtamaView();
                    } catch (Exception ex) {
                        System.err.println("Error opening main page: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
            } else {
                loginView.showMessage("Invalid OTP! Please try again.");
            }
        });

        otpDialog.setVisible(true);
    }

         public void openHalamanUtamaView() {
        System.out.println("Membuka Halaman Utama...");

        // Buat instance baru HalamanUtamaView
        HalamanUtamaView halamanUtama = new HalamanUtamaView();

        // Tambahkan listener untuk tombol profile
        halamanUtama.addProfileButtonListener(e -> {
            System.out.println("Tombol Profile diklik!");
            if (loggedInUser != null) {
                halamanUtama.dispose();
                showUserProfile(loggedInUser);
            } else {
                System.out.println("Error: User data tidak ditemukan!");
                halamanUtama.showMessage("Error: User data tidak ditemukan!");
            }
        });

        // Tambahkan listener untuk tombol kategori sampah
        halamanUtama.addKategoriSampahButtonListener(e -> {
            System.out.println("Tombol Kategori Sampah diklik!");
            halamanUtama.dispose();
            KategoriSampahView kategoriSampahView = new KategoriSampahView();
            kategoriSampahView.setVisible(true);
        });

        // Tampilkan halaman utama
        halamanUtama.setVisible(true);
        System.out.println("Halaman Utama ditampilkan");
    }{
            HalamanUtamaView halamanUtama = new HalamanUtamaView();

            // Add profile button listener
            halamanUtama.addProfileButtonListener(e -> {
                System.out.println("Profile button clicked");
                if (loggedInUser != null) {
                    halamanUtama.dispose();
                    showUserProfile(loggedInUser);
                } else {
                    halamanUtama.showMessage("Error: User data not found!");
                }
            });

            // Add kategori sampah button listener
            halamanUtama.addKategoriSampahButtonListener(e -> {
                System.out.println("Kategori Sampah button clicked");
                halamanUtama.dispose();
                KategoriSampahView kategoriSampahView = new KategoriSampahView();
                kategoriSampahView.setVisible(true);
            });

            // Show the main page
            halamanUtama.setVisible(true);
            System.out.println("HalamanUtamaView opened");
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

        // Display the profile view
        userProfileView.setVisible(true);
        System.out.println("UserProfileView opened for user: " + user.getUsername());
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
                    loggedInUser.getPhotoPath(), // Gunakan photo path yang ada di model
                    userProfileView.getAddress(),
                    userProfileView.getPhoneNumber()
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


    }
