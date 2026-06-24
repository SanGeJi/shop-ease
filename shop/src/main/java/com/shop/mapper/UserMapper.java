package com.shop.mapper;

import com.shop.entity.User;
import com.shop.entity.Session;
import org.apache.ibatis.annotations.*;

public interface UserMapper {
    @Insert("INSERT INTO users(username,password_hash,salt,role) VALUES(#{username},#{passwordHash},#{salt},#{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User u);

    @Select("SELECT * FROM users WHERE username=#{username}")
    User findByUsername(String username);

    @Select("SELECT id,username,role,phone,address,created_at FROM users WHERE id=#{id}")
    User findById(Long id);

    @Select("SELECT COUNT(*) FROM users")
    int count();

    @Select("SELECT id,username,role,phone,address,created_at FROM users ORDER BY created_at DESC")
    java.util.List<User> findAll();
}