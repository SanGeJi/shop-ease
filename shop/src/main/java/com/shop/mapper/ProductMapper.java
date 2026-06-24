package com.shop.mapper;

import com.shop.entity.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ProductMapper {
    String COLS = "p.id,p.name,p.description,p.category_id,p.image_url,p.status,p.created_at,c.name AS category_name, (SELECT MIN(s.price) FROM skus s WHERE s.product_id=p.id) AS min_price";

    @Select("<script>" +
            "SELECT " + COLS + " FROM products p LEFT JOIN categories c ON p.category_id=c.id " +
            "WHERE p.status=1 " +
            "<if test='categoryId!=null'> AND p.category_id=#{categoryId} </if>" +
            "<if test='q!=null and q!=\"\"'> AND p.name LIKE CONCAT('%',#{q},'%') </if>" +
            "ORDER BY p.created_at DESC LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Product> findOnSale(@Param("categoryId") Long categoryId, @Param("q") String q, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT " + COLS + " FROM products p LEFT JOIN categories c ON p.category_id=c.id ORDER BY p.created_at DESC")
    List<Product> findAll();

    @Select("SELECT " + COLS + " FROM products p LEFT JOIN categories c ON p.category_id=c.id WHERE p.id=#{id}")
    Product findById(Long id);

    @Insert("INSERT INTO products(name,description,category_id,image_url,status) VALUES(#{name},#{description},#{categoryId},#{imageUrl},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Product p);

    @Update("UPDATE products SET name=#{name},description=#{description},category_id=#{categoryId},image_url=#{imageUrl},status=#{status} WHERE id=#{id}")
    void update(Product p);

    @Update("UPDATE products SET image_url=#{url} WHERE id=#{id}")
    void updateImage(@Param("id") Long id, @Param("url") String url);

    @Delete("DELETE FROM products WHERE id=#{id}")
    void delete(Long id);

    @Select("SELECT COUNT(*) FROM products")
    int count();
}
