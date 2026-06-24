package com.shop.controller;

import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired private OrderMapper orderMapper;
    @Autowired private ProductMapper productMapper;
    @Autowired private UserMapper userMapper;

    @GetMapping("/dashboard")
    public Result dashboard() {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        Map<String,Object> data = new HashMap<>();
        data.put("sales", orderMapper.totalSales());
        data.put("orderCount", orderMapper.count());
        data.put("productCount", productMapper.count());
        data.put("userCount", userMapper.count());
        return Result.ok(data);
    }

    @GetMapping("/users")
    public Result users() {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        return Result.ok(Map.of("rows", userMapper.findAll()));
    }

    // 图片上传
    @PostMapping("/products/{id}/image")
    public Result uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        try {
            String base = System.getProperty("user.dir", ".");
            String ext = "";
            String name = file.getOriginalFilename();
            if (name != null && name.contains(".")) ext = name.substring(name.lastIndexOf("."));
            String filename = "p" + id + "-" + System.currentTimeMillis() + ext;
            File dir = new File(base, "uploads");
            if (!dir.exists()) dir.mkdirs();
            file.transferTo(new File(dir, filename));
            String url = "/uploads/" + filename;
            // 更新商品图片
            productMapper.updateImage(id, url);
            return Result.ok(Map.of("url", url));
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
