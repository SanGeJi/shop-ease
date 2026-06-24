package com.shop.controller;

import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.entity.Category;
import com.shop.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {
    @Autowired private CategoryMapper categoryMapper;

    @GetMapping
    public Result list() {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        return Result.ok(Map.of("rows", categoryMapper.findAll()));
    }

    @PostMapping
    public Result create(@RequestBody Category c) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        categoryMapper.insert(c);
        return Result.ok(Map.of("id", c.getId()));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Category c) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        c.setId(id);
        categoryMapper.update(c);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        categoryMapper.delete(id);
        return Result.ok();
    }
}
