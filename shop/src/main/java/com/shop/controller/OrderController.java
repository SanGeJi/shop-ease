package com.shop.controller;

import com.shop.common.AuthUtil;
import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.entity.*;
import com.shop.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired private CartMapper cartMapper;
    @Autowired private OrderMapper orderMapper;
    @Autowired private SkuMapper skuMapper;
    @Autowired private PaymentMapper paymentMapper;
    @Autowired private CouponMapper couponMapper;
    @Autowired private OrderService orderService;

    @PostMapping("/orders")
    @Transactional
    public Result create(@RequestBody Map<String,Object> body) {
        return orderService.createOrder(body);
    }

    @GetMapping("/orders")
    public Result myOrders() {
        List<Order> orders = orderMapper.findByUser(UserContext.getUserId());
        return Result.ok(Map.of("rows", orders));
    }

    @GetMapping("/orders/{id}")
    public Result detail(@PathVariable Long id) {
        Order o = orderMapper.findById(id);
        if (o == null) return Result.error(404, "订单不存在");
        if (!UserContext.isAdmin() && !o.getUserId().equals(UserContext.getUserId()))
            return Result.error(403, "无权限");
        List<OrderItem> items = orderMapper.findItems(id);
        return Result.ok(Map.of("order", o, "items", items));
    }

    // 模拟支付
    @PostMapping("/orders/{id}/pay")
    @Transactional
    public Result pay(@PathVariable Long id) {
        Order o = orderMapper.findById(id);
        if (o == null) return Result.error(404, "订单不存在");
        if (!o.getUserId().equals(UserContext.getUserId())) return Result.error(403, "无权限");
        if (!"pending".equals(o.getStatus())) return Result.error("订单状态不允许支付");
        paymentMapper.insert(id, "mock", o.getTotal(), "success");
        orderMapper.updateStatus(id, "paid");
        return Result.ok(Map.of("status", "paid"));
    }

    // admin 订单管理
    @GetMapping("/admin/orders")
    public Result adminOrders() {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        return Result.ok(Map.of("rows", orderMapper.findAll()));
    }

    @PatchMapping("/admin/orders/{id}")
    public Result adminUpdateStatus(@PathVariable Long id, @RequestBody Map<String,String> body) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        orderMapper.updateStatus(id, body.get("status"));
        return Result.ok();
    }
}