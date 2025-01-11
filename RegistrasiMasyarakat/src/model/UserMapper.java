package model;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    // Register pengguna baru
    @Insert("INSERT INTO users (username, password, email, phone_number, verification_code, verification_code_expiry, is_verified) " +
            "VALUES (#{username}, #{password}, #{email}, #{phoneNumber}, NULL, NULL, true)")
    int insertWithVerification(
            @Param("username") String username,
            @Param("password") String password,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber
    );

    // Verifikasi pengguna
    @Update("UPDATE users SET is_verified = true, verification_code = NULL, verification_code_expiry = NULL " +
            "WHERE email = #{email} AND verification_code = #{code} " +
            "AND verification_code_expiry > CURRENT_TIMESTAMP " +
            "AND is_verified = false")
    int verifyUser(@Param("email") String email, @Param("code") String code);

    // Periksa apakah pengguna sudah terverifikasi
    @Select("SELECT is_verified FROM users WHERE id = #{id}")
    boolean isUserVerified(@Param("id") int id);

    // Login dengan username dan password
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "photoPath", column = "photo_path"),
            @Result(property = "address", column = "address"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "roleId", column = "role_id") // Hanya untuk pembacaan
    })
    UserModel findByUsernameAndPassword(
            @Param("username") String username,
            @Param("password") String password
    );

    // Login dengan username saja
    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "photoPath", column = "photo_path"),
            @Result(property = "address", column = "address"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "roleId", column = "role_id") // Hanya untuk pembacaan
    })
    UserModel findByUsername(@Param("username") String username);

    // Reset password
    @Update("UPDATE users SET password = #{newPassword} WHERE email = #{email}")
    int updatePassword(@Param("email") String email, @Param("newPassword") String newPassword);

    @Select("SELECT * FROM users WHERE email = #{email}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "verificationCode", column = "verification_code"),
            @Result(property = "verificationCodeExpiry", column = "verification_code_expiry"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "photoPath", column = "photo_path"),
            @Result(property = "address", column = "address")
    })
    UserModel findByEmail(@Param("email") String email);

    // Ambil profil pengguna berdasarkan ID
    @Select("SELECT id, username, email, role_id, " +
            "photo_path AS photoPath, " +
            "address, " +
            "phone_number AS phoneNumber " +
            "FROM users WHERE id = #{id}")
    @Results({
            @Result(property = "roleId", column = "role_id") // Hanya untuk pembacaan
    })
    UserModel findById(@Param("id") int id);

    // Perbarui profil
    @Update("UPDATE users SET username = #{username}, email = #{email}, " +
            "photo_path = CASE WHEN #{photoPath} IS NOT NULL THEN #{photoPath} ELSE photo_path END, " +
            "address = #{address}, " +
            "phone_number = CASE WHEN #{phoneNumber} IS NOT NULL THEN #{phoneNumber} ELSE phone_number END, " +
            "role_id = #{roleId} " +  // Add role_id field to the update query
            "WHERE id = #{id}")
    int updateProfile(@Param("id") int id, @Param("username") String username,
                      @Param("email") String email, @Param("photoPath") String photoPath,
                      @Param("address") String address, @Param("phoneNumber") String phoneNumber,
                      @Param("roleId") int roleId);  // Add roleId to the method parameters


    // Perbarui foto pengguna
    @Update("UPDATE users SET photo_path = #{photoPath} WHERE id = #{id}")
    int updatePhotoPath(@Param("id") int id, @Param("photoPath") String photoPath);

    // Hapus pengguna
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteUser(@Param("id") int id);

    @Select("SELECT * FROM users ORDER BY id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "address", column = "address"),
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "photoPath", column = "photo_path"),
            @Result(property = "isVerified", column = "is_verified")
    })
    List<UserModel> getAllUsers();
}
