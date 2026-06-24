package com.shop.mapper;

import com.shop.entity.Session;
import org.apache.ibatis.annotations.*;

public interface SessionMapper {
    @Insert("INSERT INTO sessions(token,user_id,role,expires_at) VALUES(#{token},#{userId},#{role},#{expiresAt})")
    void insert(Session s);

    @Select("SELECT token,user_id,role,expires_at FROM sessions WHERE token=#{token} AND expires_at > NOW()")
    Session findByToken(String token);

    @Delete("DELETE FROM sessions WHERE token=#{token}")
    void deleteByToken(String token);
}