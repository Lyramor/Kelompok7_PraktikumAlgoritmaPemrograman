package model;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.*;

public interface UserMapper {
    
    //Register
    @Insert("INSERT INTO users (username, password, email, phone_number, verification_code, verification_code_expiry, is_verified) " +
            "VALUES (#{username}, #{password}, #{email}, #{phoneNumber}, NULL, NULL, true)")
    int insertWithVerification(
            @Param("username") String username,
            @Param("password") String password,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber
    );
    @Update("UPDATE users SET is_verified = true, verification_code = NULL, verification_code_expiry = NULL " +
            "WHERE email = #{email} AND verification_code = #{code} " +
            "AND verification_code_expiry > CURRENT_TIMESTAMP " +
            "AND is_verified = false")
    int verifyUser(@Param("email") String email, @Param("code") String code);

    @Select("SELECT is_verified FROM users WHERE id = #{id}")
    boolean isUserVerified(@Param("id") int id);

    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "photoPath", column = "photo_path"),
            @Result(property = "address", column = "address"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "isVerified", column = "is_verified")
    })
    UserModel findByUsernameAndPassword(
            @Param("username") String username,
            @Param("password") String password
    );
    //Login
    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "photoPath", column = "photo_path"),
            @Result(property = "address", column = "address"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "isVerified", column = "is_verified")
    })
    UserModel findByUsername(@Param("username") String username);


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

    // Fitur lupa password
    @Update("UPDATE users SET password = #{newPassword} WHERE email = #{email}")
    int updatePassword(@Param("email") String email, @Param("newPassword") String newPassword);

    
    @Select("SELECT id, username, email, " +
        "photo_path AS photoPath, " + // Alias sesuai dengan atribut di UserModel
        "address, " +
        "phone_number AS phoneNumber " + // Alias sesuai dengan atribut di UserModel
        "FROM users WHERE id = #{id}")
    UserModel findById(@Param("id") int id);


    @Update("UPDATE users SET username = #{username}, email = #{email}, " +
            "photo_path = CASE WHEN #{photoPath} IS NOT NULL THEN #{photoPath} ELSE photo_path END, " +
            "address = #{address}, " +
            "phone_number = CASE WHEN #{phoneNumber} IS NOT NULL THEN #{phoneNumber} ELSE phone_number END " +
            "WHERE id = #{id}")
    int updateProfile(@Param("id") int id, @Param("username") String username,
                     @Param("email") String email, @Param("photoPath") String photoPath,
                     @Param("address") String address, @Param("phoneNumber") String phoneNumber);

    @Update("UPDATE users SET photo_path = #{photoPath} WHERE id = #{id}")
    int updatePhotoPath(@Param("id") int id, @Param("photoPath") String photoPath);




}
