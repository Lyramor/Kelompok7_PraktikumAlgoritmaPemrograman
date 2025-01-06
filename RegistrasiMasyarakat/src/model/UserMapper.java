package model;

import org.apache.ibatis.annotations.*;

public interface UserMapper {

        @Select("SELECT id, username, email, " +
        "photo_path AS photoPath, " + // Alias sesuai dengan atribut di UserModel
        "address, " +
        "phone_number AS phoneNumber " + // Alias sesuai dengan atribut di UserModel
            "FROM users WHERE username = #{username} AND password = #{password}")
    UserModel findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    
    @Select("SELECT id, username, email, " +
        "photo_path AS photoPath, " + // Alias sesuai dengan atribut di UserModel
        "address, " +
        "phone_number AS phoneNumber " + // Alias sesuai dengan atribut di UserModel
        "FROM users WHERE username = #{username}")
UserModel findByUsername(@Param("username") String username);

    
       @Select("SELECT id, username, email, " +
        "photo_path AS photoPath, " + // Alias sesuai dengan atribut di UserModel
        "address, " +
        "phone_number AS phoneNumber " + // Alias sesuai dengan atribut di UserModel
            "FROM users WHERE email = #{email}")
    UserModel findByEmail(@Param("email") String email);

    @Insert("INSERT INTO users (username, password, email) VALUES (#{username}, #{password}, #{email})")
    int insert(@Param("username") String username, @Param("password") String password, @Param("email") String email);

    // Fitur lupa password
    @Update("UPDATE users SET password = #{newPassword} WHERE email = #{email}")
    int updatePassword(@Param("email") String email, @Param("newPassword") String newPassword);

//    @Select("SELECT id, username, email, " +
//            "COALESCE(photo_path, 'default_photo.jpg') AS photo_path, " +
//            "address, " +
//            "COALESCE(phone_number, 'Not Provided') AS phone_number " +
//            "FROM users WHERE id = #{id}")
//    UserModel findById(@Param("id") int id);
    
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
