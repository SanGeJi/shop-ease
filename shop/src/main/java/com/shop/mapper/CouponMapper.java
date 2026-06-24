package com.shop.mapper;

import com.shop.entity.Coupon;
import com.shop.entity.UserCoupon;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface CouponMapper {
    @Select("SELECT * FROM coupons WHERE status=1 AND claimed < total ORDER BY created_at DESC")
    List<Coupon> findAvailable();

    @Select("SELECT * FROM coupons ORDER BY created_at DESC")
    List<Coupon> findAll();

    @Select("SELECT * FROM coupons WHERE id=#{id}")
    Coupon findById(Long id);

    @Insert("INSERT INTO coupons(code,name,type,value,min_spend,total,claimed,status) VALUES(#{code},#{name},#{type},#{value},#{minSpend},#{total},0,1)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Coupon c);

    @Update("UPDATE coupons SET claimed=claimed+1 WHERE id=#{id} AND claimed < total")
    int claimOne(Long id);

    @Delete("DELETE FROM coupons WHERE id=#{id}")
    void delete(Long id);

    @Select("SELECT uc.*,c.code,c.name,c.type,c.value,c.min_spend FROM user_coupons uc " +
            "JOIN coupons c ON uc.coupon_id=c.id WHERE uc.user_id=#{userId} ORDER BY uc.claimed_at DESC")
    List<UserCoupon> findByUser(Long userId);

    @Select("SELECT * FROM user_coupons WHERE user_id=#{userId} AND coupon_id=#{couponId}")
    UserCoupon findUserCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);

    @Insert("INSERT INTO user_coupons(user_id,coupon_id,status) VALUES(#{userId},#{couponId},'unused')")
    void insertUserCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);

    @Update("UPDATE user_coupons SET status='used' WHERE user_id=#{userId} AND coupon_id=#{couponId}")
    void markUsed(@Param("userId") Long userId, @Param("couponId") Long couponId);
}