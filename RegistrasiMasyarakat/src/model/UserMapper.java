package model;

import org.apache.ibatis.annotations.*;

public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    UserModel findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    
    @Select("SELECT * FROM users WHERE username = #{username}")
    UserModel findByUsername(@Param("username") String username);
    
    @Select("SELECT * FROM users WHERE email = #{email}")
    UserModel findByEmail(@Param("email") String email);
    
    @Insert("INSERT INTO users (username, password, email) VALUES (#{username}, #{password}, #{email})")
    int insert(@Param("username") String username, @Param("password") String password, @Param("email") String email);

    //fitur lupa password
    @Update("UPDATE users SET password = #{newPassword} WHERE email = #{email}")
    int updatePassword(@Param("email") String email, @Param("newPassword") String newPassword);

}