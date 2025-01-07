package controller;

import model.UserMapper;
import model.UserModel;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

import java.sql.Timestamp;

public class TokenController {

    public boolean verifyOTP(String email, String otpInput) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            UserModel user = userMapper.findByEmail(email);

            if (user == null) {
                System.out.println("Error: User not found for email: " + email);
                return false;
            }

            // Debug log
            System.out.println("Database OTP: " + user.getVerificationCode());
            System.out.println("Input OTP: " + otpInput);
            System.out.println("OTP expiry time: " + user.getVerificationCodeExpiry());
            System.out.println("Current time: " + new Timestamp(System.currentTimeMillis()));

            // Periksa apakah kode verifikasi sudah kadaluarsa
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (user.getVerificationCodeExpiry() != null && currentTime.after(user.getVerificationCodeExpiry())) {
                System.out.println("Error: OTP has expired.");
                return false;
            }

            // Periksa apakah OTP cocok
            if (otpInput.trim().equals(user.getVerificationCode().trim())) {
                int result = userMapper.verifyUser(email, otpInput);
                session.commit();

                if (result > 0) {
                    System.out.println("User verified successfully!");
                    return true;
                } else {
                    System.out.println("Error: Failed to update verification status in database.");
                }
            } else {
                System.out.println("Error: OTP does not match.");
            }
        } catch (Exception ex) {
            System.err.println("Exception during OTP verification: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
}

