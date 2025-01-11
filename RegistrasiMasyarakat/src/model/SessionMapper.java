package model;

import java.sql.Timestamp;
import org.apache.ibatis.annotations.*;

public interface SessionMapper {
    @Insert("INSERT INTO sessions (user_id, token, expires_at) VALUES (#{userId}, #{token}, #{expiresAt})")
    int insert(@Param("userId") int userId, @Param("token") String token, @Param("expiresAt") Timestamp expiresAt);

    @Select("SELECT * FROM sessions WHERE token = #{token} AND expires_at > CURRENT_TIMESTAMP AND is_active = true")
    Session findByToken(@Param("token") String token);

    @Update("UPDATE sessions SET is_active = false WHERE user_id = #{userId}")
    int deactivateUserSessions(@Param("userId") int userId);
}
