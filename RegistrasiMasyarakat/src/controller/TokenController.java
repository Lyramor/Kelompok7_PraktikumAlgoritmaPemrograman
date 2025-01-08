package controller;

import model.OTPVerification;
import model.OTPVerificationMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

import java.sql.Timestamp;

public class TokenController {
    public boolean verifyOTP(String email, String otpInput) {
        if (email == null || otpInput == null || email.trim().isEmpty() || otpInput.trim().isEmpty()) {
            System.out.println("Error: Email or OTP input is null or empty");
            return false;
        }

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            OTPVerificationMapper otpMapper = session.getMapper(OTPVerificationMapper.class);

            // Get the latest unverified OTP verification record
            OTPVerification verification = otpMapper.findByEmail(email);

            // Debug log
            System.out.println("Debug - Verification found: " + (verification != null));
            if (verification != null) {
                System.out.println("Debug - DB OTP Code: " + verification.getOtpCode());
                System.out.println("Debug - DB is_verified: " + verification.isVerified());
            }

            if (verification == null) {
                System.out.println("Error: No OTP verification found for email: " + email);
                return false;
            }

            // Validate OTP code
            if (!otpInput.equals(verification.getOtpCode())) {
                System.out.println("Error: Invalid OTP");
                return false;
            }

            // Validate expiry
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (currentTime.after(verification.getExpiresAt())) {
                System.out.println("Error: OTP has expired");
                return false;
            }

            // If OTP is valid, update is_verified to true
            int rowsUpdated = otpMapper.markAsVerified(email);
            if (rowsUpdated > 0) {
                System.out.println("Success: OTP marked as verified.");
                session.commit();
                return true;
            } else {
                System.out.println("Error: Failed to update OTP verification status.");
                session.rollback();
                return false;
            }

        } catch (Exception ex) {
            System.err.println("Exception during OTP verification: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}
