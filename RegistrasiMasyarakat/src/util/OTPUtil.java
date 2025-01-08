package util;

import java.util.Random;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class OTPUtil {
    private static final int OTP_LENGTH = 6;
    
    // Generate OTP
    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        // Tambah log untuk memastikan OTP dibuat
        String generatedOTP = otp.toString();
        System.out.println("Debug - Generated OTP: " + generatedOTP);

        return generatedOTP;
    }
    
    // Send OTP via email
    public static void sendOTPEmail(String email, String otp) throws MessagingException {
        // Konfigurasi email pengirim (gunakan email Gmail Anda)
        String senderEmail = "mmarsanj2435@gmail.com"; // Ganti dengan email Anda
        String senderPassword = "hxbg rgyn siog gyof"; // Ganti dengan App Password dari Gmail
        
        // Setup mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        
        // Create session with authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        
        // Create message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Your Login OTP");
        message.setText("Your OTP for login is: " + otp + "\nThis OTP will expire in 5 minutes.");
        
        // Send message
        Transport.send(message);
    }
}