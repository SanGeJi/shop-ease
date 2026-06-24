package com.shop.mapper;

import com.shop.entity.CartItem;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface CartMapper {
    String COLS = "c.id,c.user_id,c.sku_id,c.quantity,c.created_at, " +
            "p.id AS product_id,p.name AS product_name,p.image_url AS product_image, " +
            "s.price,s.stock";

    @Select("SELECT " + COLS + " FROM cart_items c " +
            "JOIN skus s ON c.sku_id=s.id " +
            "JOIN products p ON s.product_id=p.id " +
            "WHERE c.user_id=#{userId} ORDER BY c.created_at DESC")
    List<CartItem> findByUser(Long userId);

    @Select("SELECT * FROM cart_items WHERE user_id=#{userId} AND sku_id=#{skuId}")
    CartItem findByUserAndSku(@Param("userId") Long userId, @Param("skuId") Long skuId);

    @Insert("INSERT INTO cart_items(user_id,sku_id,quantity) VALUES(#{userId},#{skuId},#{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CartItem c);

    @Update("UPDATE cart_items SET quantity=#{quantity} WHERE id=#{id}")
    void updateQty(CartItem c);

    @Delete("DELETE FROM cart_items WHERE id=#{id}")
    void delete(Long id);

    @Delete("DELETE FROM cart_items WHERE user_id=#{userId}")
    void deleteByUser(Long userId);
}