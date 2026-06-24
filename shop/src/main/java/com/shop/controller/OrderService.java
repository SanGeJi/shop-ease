package com.shop.controller;

import com.shop.common.AuthUtil;
import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.entity.*;
import com.shop.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {
    @Autowired private CartMapper cartMapper;
    @Autowired private OrderMapper orderMapper;
    @Autowired private SkuMapper skuMapper;
    @Autowired private CouponMapper couponMapper;

    @Transactional
    public Result createOrder(Map<String,Object> body) {
        Long userId = UserContext.getUserId();
        List<CartItem> cart = cartMapper.findByUser(userId);
        if (cart.isEmpty()) return Result.error("购物车为空");

        String receiver = (String) body.get("receiver");
        String phone = (String) body.get("phone");
        String address = (String) body.get("address");
        if (receiver == null || phone == null || address == null) return Result.error("请填写收货信息");

        // 扣库存 + 算总额
        long subtotal = 0;
        for (CartItem c : cart) {
            int affected = skuMapper.deductStock(c.getSkuId(), c.getQuantity());
            if (affected == 0) return Result.error("库存不足: " + c.getProductName());
            subtotal += c.getPrice() * c.getQuantity();
        }

        // 优惠券
        Long couponId = body.get("coupon_id") != null ? ((Number) body.get("coupon_id")).longValue() : null;
        long discount = 0;
        if (couponId != null) {
            UserCoupon uc = couponMapper.findUserCoupon(userId, couponId);
            if (uc == null || !"unused".equals(uc.getStatus())) return Result.error("优惠券不可用");
            Coupon coupon = couponMapper.findById(couponId);
            if (subtotal < coupon.getMinSpend()) return Result.error("未达到优惠券使用门槛");
            if ("fixed".equals(coupon.getType())) discount = coupon.getValue();
            else if ("percent".equals(coupon.getType())) discount = subtotal - subtotal * coupon.getValue() / 100;
        }
        long total = subtotal - discount;
        if (total < 0) total = 0;

        // 建订单
        Order o = new Order();
        o.setOrderNo(AuthUtil.genOrderNo());
        o.setUserId(userId);
        o.setTotal(total);
        o.setDiscount(discount);
        o.setStatus("pending");
        o.setReceiver(receiver);
        o.setPhone(phone);
        o.setAddress(address);
        o.setCouponId(couponId);
        orderMapper.insert(o);

        // 订单明细
        for (CartItem c : cart) {
            OrderItem item = new OrderItem();
            item.setOrderId(o.getId());
            item.setSkuId(c.getSkuId());
            item.setProductName(c.getProductName());
            item.setSkuSpecs(c.getSkuSpecs() != null ? c.getSkuSpecs() : "");
            item.setPrice(c.getPrice());
            item.setQuantity(c.getQuantity());
            orderMapper.insertItem(item);
        }

        // 标记优惠券已用
        if (couponId != null) couponMapper.markUsed(userId, couponId);
        // 清购物车
        cartMapper.deleteByUser(userId);

        return Result.ok(Map.of("order", o));
    }
}