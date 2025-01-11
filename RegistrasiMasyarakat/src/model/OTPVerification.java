package model;

import java.sql.Timestamp;

public class OTPVerification {
    private int id;
    private String email;
    private String otpCode;
    private Timestamp expiresAt;
    private boolean isVerified;
    // Getters dan Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }



    @Override
    public String toString() {
        return "OTPVerification{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", otpCode='" + otpCode + '\'' +
                ", expiresAt=" + expiresAt +
                ", isVerified=" + isVerified +
                '}';
    }
}
