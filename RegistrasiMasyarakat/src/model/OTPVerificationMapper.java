    package model;

    import java.sql.Timestamp;
    import org.apache.ibatis.annotations.*;

    public interface OTPVerificationMapper {
        // Insert OTP dengan timestamp expiry (15 menit dari sekarang)
        @Insert("INSERT INTO otp_verifications (email, otp_code, expires_at) " +
                "VALUES (#{email}, #{otpCode}, #{expiresAt})")
        int insert(@Param("email") String email, @Param("otpCode") String otpCode, @Param("expiresAt") Timestamp expiresAt);

        @Select("SELECT id, email, otp_code AS otpCode, expires_at AS expiresAt, is_verified AS isVerified " +
                "FROM otp_verifications WHERE email = #{email} AND is_verified = false LIMIT 1")
        OTPVerification findByEmail(@Param("email") String email);

        @Update("UPDATE otp_verifications SET is_verified = true " +
                "WHERE email = #{email} AND is_verified = false")
        int markAsVerified(@Param("email") String email);

        @Delete("DELETE FROM otp_verifications WHERE email = #{email}")
        int deleteByEmail(@Param("email") String email);
    }
