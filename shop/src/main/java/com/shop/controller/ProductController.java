package com.shop.controller;

import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.entity.*;
import com.shop.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired private ProductMapper productMapper;
    @Autowired private CategoryMapper categoryMapper;
    @Autowired private SkuMapper skuMapper;
    @Autowired private ProductSpecMapper specMapper;

    // ===== 前台 =====
    @GetMapping("/products")
    public Result list(@RequestParam(required=false) Long category, @RequestParam(required=false) String q,
                       @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="12") int size) {
        int offset = (page - 1) * size;
        List<Product> rows = productMapper.findOnSale(category, q, offset, size);
        return Result.ok(Map.of("rows", rows));
    }

    @GetMapping("/products/{id}")
    public Result detail(@PathVariable Long id) {
        Product p = productMapper.findById(id);
        if (p == null) return Result.error(404, "商品不存在");
        List<Sku> skus = skuMapper.findByProduct(id);
        List<ProductSpec> specs = specMapper.findSpecs(id);
        Map<Long, List<SpecOption>> specOptions = new LinkedHashMap<>();
        for (ProductSpec s : specs) specOptions.put(s.getId(), specMapper.findOptions(s.getId()));
        List<SkuValue> values = skuMapper.findValuesByProduct(id);
        return Result.ok(Map.of("product", p, "skus", skus, "specs", specs, "specOptions", specOptions, "skuValues", values));
    }

    @GetMapping("/categories")
    public Result categories() {
        return Result.ok(Map.of("rows", categoryMapper.findAll()));
    }

    // ===== 后台:商品 CRUD =====
    @GetMapping("/admin/products")
    public Result adminList() {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        return Result.ok(Map.of("rows", productMapper.findAll()));
    }

    @PostMapping("/admin/products")
    public Result adminCreate(@RequestBody Product p) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        if (p.getStatus() == null) p.setStatus(1);
        productMapper.insert(p);
        return Result.ok(Map.of("id", p.getId()));
    }

    @PutMapping("/admin/products/{id}")
    public Result adminUpdate(@PathVariable Long id, @RequestBody Product p) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        p.setId(id);
        productMapper.update(p);
        return Result.ok();
    }

    @DeleteMapping("/admin/products/{id}")
    public Result adminDelete(@PathVariable Long id) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        // 删 SKU 和规格
        skuMapper.deleteValuesByProduct(id);
        skuMapper.deleteByProduct(id);
        specMapper.deleteOptionsByProduct(id);
        specMapper.deleteSpecsByProduct(id);
        productMapper.delete(id);
        return Result.ok();
    }

    // ===== 后台:SKU + 规格 管理 =====
    @GetMapping("/admin/products/{id}/skus")
    public Result skuDetail(@PathVariable Long id) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        List<Sku> skus = skuMapper.findByProduct(id);
        List<ProductSpec> specs = specMapper.findSpecs(id);
        Map<Long, List<SpecOption>> specOptions = new LinkedHashMap<>();
        for (ProductSpec s : specs) specOptions.put(s.getId(), specMapper.findOptions(s.getId()));
        List<SkuValue> values = skuMapper.findValuesByProduct(id);
        return Result.ok(Map.of("skus", skus, "specs", specs, "specOptions", specOptions, "skuValues", values));
    }

    @PutMapping("/admin/products/{id}/skus")
    public Result skuSave(@PathVariable Long id, @RequestBody Map<String,Object> body) {
        if (!UserContext.isAdmin()) return Result.error(403, "无权限");
        // 先删旧
        skuMapper.deleteValuesByProduct(id);
        skuMapper.deleteByProduct(id);
        specMapper.deleteOptionsByProduct(id);
        specMapper.deleteSpecsByProduct(id);
        // specs: [{name, options:[...]}]
        List<Map<String,Object>> specs = (List<Map<String,Object>>) body.get("specs");
        List<Map<String,Object>> skus = (List<Map<String,Object>>) body.get("skus");
        Map<String, Long> specNameToId = new HashMap<>();
        Map<String, Long> optionKeyToId = new HashMap<>();
        if (specs != null) {
            for (Map<String,Object> sp : specs) {
                ProductSpec ps = new ProductSpec();
                ps.setProductId(id);
                ps.setName((String) sp.get("name"));
                specMapper.insertSpec(ps);
                specNameToId.put(ps.getName(), ps.getId());
                List<String> opts = (List<String>) sp.get("options");
                if (opts != null) {
                    for (String val : opts) {
                        SpecOption so = new SpecOption();
                        so.setSpecId(ps.getId());
                        so.setValue(val);
                        specMapper.insertOption(so);
                        optionKeyToId.put(ps.getName() + ":" + val, so.getId());
                    }
                }
            }
        }
        // skus: [{price, stock, options:{specName: optionValue}}]
        if (skus != null) {
            for (Map<String,Object> sk : skus) {
                Sku s = new Sku();
                s.setProductId(id);
                s.setSkuCode("SKU-" + id + "-" + System.nanoTime());
                s.setPrice(((Number) sk.get("price")).longValue());
                s.setStock(((Number) sk.get("stock")).intValue());
                skuMapper.insert(s);
                Map<String,String> opts = (Map<String,String>) sk.get("options");
                if (opts != null) {
                    for (Map.Entry<String,String> e : opts.entrySet()) {
                        Long specId = specNameToId.get(e.getKey());
                        Long optId = optionKeyToId.get(e.getKey() + ":" + e.getValue());
                        if (specId != null && optId != null) {
                            SkuValue sv = new SkuValue();
                            sv.setSkuId(s.getId());
                            sv.setSpecId(specId);
                            sv.setOptionId(optId);
                            skuMapper.insertSkuValue(sv);
                        }
                    }
                }
            }
        }
        return Result.ok();
    }
}