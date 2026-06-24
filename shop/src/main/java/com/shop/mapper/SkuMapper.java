package com.shop.mapper;

import com.shop.entity.Sku;
import com.shop.entity.SkuValue;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface SkuMapper {
    @Select("SELECT * FROM skus WHERE product_id=#{productId}")
    List<Sku> findByProduct(Long productId);

    @Select("SELECT * FROM skus WHERE id=#{id}")
    Sku findById(Long id);

    @Insert("INSERT INTO skus(product_id,sku_code,price,stock) VALUES(#{productId},#{skuCode},#{price},#{stock})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Sku s);

    @Update("UPDATE skus SET price=#{price},stock=#{stock} WHERE id=#{id}")
    void update(Sku s);

    @Delete("DELETE FROM skus WHERE id=#{id}")
    void delete(Long id);

    @Update("UPDATE skus SET stock=stock-#{qty} WHERE id=#{id} AND stock>=#{qty}")
    int deductStock(@Param("id") Long id, @Param("qty") int qty);

    // SKU 规格值
    @Insert("INSERT INTO sku_values(sku_id,spec_id,option_id) VALUES(#{skuId},#{specId},#{optionId})")
    void insertSkuValue(SkuValue v);

    @Select("SELECT sv.id,sv.sku_id,sv.spec_id,sv.option_id,ps.name AS spec_name,so.value AS option_value " +
            "FROM sku_values sv " +
            "JOIN product_specs ps ON sv.spec_id=ps.id " +
            "JOIN product_spec_options so ON sv.option_id=so.id " +
            "WHERE sv.sku_id IN (SELECT id FROM skus WHERE product_id=#{productId})")
    List<SkuValue> findValuesByProduct(Long productId);

    @Delete("DELETE FROM sku_values WHERE sku_id IN (SELECT id FROM skus WHERE product_id=#{productId})")
    void deleteValuesByProduct(Long productId);

    @Delete("DELETE FROM skus WHERE product_id=#{productId}")
    void deleteByProduct(Long productId);
}