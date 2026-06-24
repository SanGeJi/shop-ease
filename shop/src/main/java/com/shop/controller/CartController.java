package com.shop.controller;

import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.entity.CartItem;
import com.shop.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired private CartMapper cartMapper;

    @GetMapping
    public Result list() {
        List<CartItem> items = cartMapper.findByUser(UserContext.getUserId());
        long total = 0;
        for (CartItem c : items) total += c.getPrice() * c.getQuantity();
        return Result.ok(Map.of("items", items, "total", total));
    }

    @PostMapping
    public Result add(@RequestBody Map<String,Object> body) {
        Long skuId = ((Number) body.get("sku_id")).longValue();
        int qty = body.get("quantity") != null ? ((Number) body.get("quantity")).intValue() : 1;
        CartItem existing = cartMapper.findByUserAndSku(UserContext.getUserId(), skuId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + qty);
            cartMapper.updateQty(existing);
        } else {
            CartItem c = new CartItem();
            c.setUserId(UserContext.getUserId());
            c.setSkuId(skuId);
            c.setQuantity(qty);
            cartMapper.insert(c);
        }
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Map<String,Object> body) {
        int qty = ((Number) body.get("quantity")).intValue();
        CartItem c = new CartItem();
        c.setId(id);
        c.setQuantity(qty);
        cartMapper.updateQty(c);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        cartMapper.delete(id);
        return Result.ok();
    }
}