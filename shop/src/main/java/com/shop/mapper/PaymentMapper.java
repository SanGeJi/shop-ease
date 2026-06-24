package com.shop.mapper;

import org.apache.ibatis.annotations.*;

public interface PaymentMapper {
    @Insert("INSERT INTO payments(order_id,method,amount,status) VALUES(#{orderId},#{method},#{amount},#{status})")
    void insert(@Param("orderId") Long orderId, @Param("method") String method, @Param("amount") long amount, @Param("status") String status);
}