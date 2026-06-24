package com.shop.controller;

import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.entity.Coupon;
import com.shop.mapper.CouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CouponController {
    @Autowired private CouponMapper couponMapper;

    // 顾客:可领的券
    @GetMapping("/coupons")
    public Result available() {
        return Result.ok(Map.of("rows", couponMapper.findAvailable()));
    }

    // 顾客:领券
    @PostMapping("/coupons/{id}/claim")
    public Result claim(@PathVariable Long id) {
        int affected = couponMapper.claimOne(id);
        if (affected == 0) return Result.error("优惠券已被领完");
        couponMapper.insertUserCoupon(UserContext.getUserId(), id);
        return Result.ok();
    }

    // 顾客:我的券
    @GetMapping("/my/coupons")
    public Result myCoupons() {
        return Result.ok(Map.of("rows", couponMapper.findByUser(UserContext.getUserId())));
    }

    // admin:券管理
    @GetMapping("/admin/coupons")
    public Result adminList() {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        return Result.ok(Map.of("rows", couponMapper.findAll()));
    }

    @PostMapping("/admin/coupons")
    public Result adminCreate(@RequestBody Coupon c) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        couponMapper.insert(c);
        return Result.ok(Map.of("id", c.getId()));
    }

    @DeleteMapping("/admin/coupons/{id}")
    public Result adminDelete(@PathVariable Long id) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        couponMapper.delete(id);
        return Result.ok();
    }
}