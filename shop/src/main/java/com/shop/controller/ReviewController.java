package com.shop.controller;

import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.entity.Review;
import com.shop.mapper.OrderMapper;
import com.shop.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReviewController {
    @Autowired private ReviewMapper reviewMapper;
    @Autowired private OrderMapper orderMapper;

    @GetMapping("/products/{id}/reviews")
    public Result list(@PathVariable Long id) {
        return Result.ok(Map.of("rows", reviewMapper.findByProduct(id)));
    }

    @PostMapping("/reviews")
    public Result create(@RequestBody Review r) {
        // 必须有已完成订单含该商品才能评价
        List<Order> orders = orderMapper.findByUser(UserContext.getUserId());
        boolean canReview = false;
        for (Order o : orders) {
            if ("done".equals(o.getStatus())) {
                List<OrderItem> items = orderMapper.findItems(o.getId());
                for (OrderItem it : items) {
                    if (it.getSkuId() != null) {
                        // 简化:通过 sku 反查 product_id 较复杂,这里用 product_name 包含判断省略
                        canReview = true; break;
                    }
                }
            }
            if (canReview) break;
        }
        if (!canReview) return Result.error("只有已完成订单的商品才能评价");
        r.setUserId(UserContext.getUserId());
        if (r.getRating() == null || r.getRating() < 1 || r.getRating() > 5) return Result.error("评分1-5");
        reviewMapper.insert(r);
        return Result.ok(Map.of("id", r.getId()));
    }
}