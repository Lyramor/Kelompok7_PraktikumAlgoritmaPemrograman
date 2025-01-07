package model;

import java.sql.Timestamp;
import org.apache.ibatis.annotations.*;

public interface TokenMapper {
    @Insert("INSERT INTO tokens (user_id, token, expires_at) VALUES (#{userId}, #{token}, #{expiresAt})")
    int insertToken(@Param("userId") int userId, @Param("token") String token, @Param("expiresAt") Timestamp expiresAt);
    
    @Select("SELECT * FROM tokens WHERE token = #{token} AND expires_at > CURRENT_TIMESTAMP")
    TokenModel findValidToken(@Param("token") String token);
    
    @Delete("DELETE FROM tokens WHERE user_id = #{userId}")
    int deleteUserTokens(@Param("userId") int userId);
}