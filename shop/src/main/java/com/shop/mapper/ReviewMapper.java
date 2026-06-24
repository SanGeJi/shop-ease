package com.shop.mapper;

import com.shop.entity.Review;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ReviewMapper {
    @Insert("INSERT INTO reviews(product_id,user_id,rating,content) VALUES(#{productId},#{userId},#{rating},#{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Review r);

    @Select("SELECT r.*,u.username FROM reviews r JOIN users u ON r.user_id=u.id WHERE r.product_id=#{productId} ORDER BY r.created_at DESC")
    List<Review> findByProduct(Long productId);
}