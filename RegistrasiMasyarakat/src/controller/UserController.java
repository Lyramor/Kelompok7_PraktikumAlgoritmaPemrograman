package controller;

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
import view.ForgotPasswordView;
import view.ResetPasswordDialog;

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
    
    private void verifyOTP(UserModel user) {
        OTPDialog otpDialog = new OTPDialog(loginView);
        otpDialog.addVerifyListener(e -> {
            String enteredOTP = otpDialog.getOTP();
            long currentTime = System.currentTimeMillis();
            
            // Check if OTP is expired
            if (currentTime - otpGenerationTime > OTP_VALIDITY_DURATION) {
                loginView.showMessage("OTP has expired! Please login again.");
                otpDialog.dispose();
                return;
            }
            
            // Verify OTP
            if (enteredOTP.equals(currentOTP)) {
                otpDialog.setVerified(true);
                otpDialog.dispose();
                loginView.showMessage("Login successful!");
                // Add your post-login logic here
            } else {
                loginView.showMessage("Invalid OTP! Please try again.");
            }
        });
        
        otpDialog.setVisible(true);
        
        // Check if verification was successful
        if (!otpDialog.isVerified()) {
            loginView.showMessage("Login cancelled or failed!");
        }
    }
    
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
}
