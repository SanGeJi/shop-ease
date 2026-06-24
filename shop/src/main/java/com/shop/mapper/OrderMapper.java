package com.shop.mapper;

import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface OrderMapper {
    @Insert("INSERT INTO orders(order_no,user_id,total,discount,status,receiver,phone,address,coupon_id) " +
            "VALUES(#{orderNo},#{userId},#{total},#{discount},#{status},#{receiver},#{phone},#{address},#{couponId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order o);

    @Insert("INSERT INTO order_items(order_id,sku_id,product_name,sku_specs,price,quantity) " +
            "VALUES(#{orderId},#{skuId},#{productName},#{skuSpecs},#{price},#{quantity})")
    void insertItem(OrderItem item);

    @Select("SELECT o.*,u.username FROM orders o JOIN users u ON o.user_id=u.id WHERE o.user_id=#{userId} ORDER BY o.created_at DESC")
    List<Order> findByUser(Long userId);

    @Select("SELECT o.*,u.username FROM orders o JOIN users u ON o.user_id=u.id ORDER BY o.created_at DESC")
    List<Order> findAll();

    @Select("SELECT * FROM orders WHERE id=#{id}")
    Order findById(Long id);

    @Update("UPDATE orders SET status=#{status} WHERE id=#{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Select("SELECT * FROM order_items WHERE order_id=#{orderId}")
    List<OrderItem> findItems(Long orderId);

    @Select("SELECT COUNT(*) FROM orders")
    int count();

    @Select("SELECT COALESCE(SUM(total),0) FROM orders WHERE status IN ('paid','shipped','done')")
    long totalSales();
}