package com.shop.mapper;

import com.shop.entity.Category;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface CategoryMapper {
    @Select("SELECT * FROM categories ORDER BY id")
    List<Category> findAll();

    @Insert("INSERT INTO categories(name,icon) VALUES(#{name},#{icon})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Category c);

    @Update("UPDATE categories SET name=#{name},icon=#{icon} WHERE id=#{id}")
    void update(Category c);

    @Delete("DELETE FROM categories WHERE id=#{id}")
    void delete(Long id);
}