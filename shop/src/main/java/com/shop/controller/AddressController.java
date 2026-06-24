package com.shop.controller;

import com.shop.common.Result;
import com.shop.common.UserContext;
import com.shop.entity.Address;
import com.shop.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired private AddressMapper addressMapper;

    @GetMapping("/addresses")
    public Result list() {
        Long uid = UserContext.getUserId();
        List<Address> rows = addressMapper.findByUser(uid);
        return Result.ok(Map.of("rows", rows));
    }

    @PostMapping("/addresses")
    public Result create(@RequestBody Address a) {
        a.setUserId(UserContext.getUserId());
        if (a.getIsDefault() == null) a.setIsDefault(0);
        if (a.getIsDefault() == 1) addressMapper.clearDefault(a.getUserId());
        addressMapper.insert(a);
        return Result.ok(Map.of("id", a.getId()));
    }

    @PutMapping("/addresses/{id}")
    public Result update(@PathVariable Long id, @RequestBody Address a) {
        Long uid = UserContext.getUserId();
        a.setId(id);
        a.setUserId(uid);
        if (a.getIsDefault() == 1) addressMapper.clearDefault(uid);
        addressMapper.update(a);
        return Result.ok();
    }

    @DeleteMapping("/addresses/{id}")
    public Result delete(@PathVariable Long id) {
        addressMapper.delete(id, UserContext.getUserId());
        return Result.ok();
    }
}
