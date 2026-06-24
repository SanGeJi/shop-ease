package com.shop.mapper;

import com.shop.entity.ProductSpec;
import com.shop.entity.SpecOption;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ProductSpecMapper {
    @Select("SELECT * FROM product_specs WHERE product_id=#{productId}")
    List<ProductSpec> findSpecs(Long productId);

    @Select("SELECT * FROM product_spec_options WHERE spec_id=#{specId}")
    List<SpecOption> findOptions(Long specId);

    @Insert("INSERT INTO product_specs(product_id,name) VALUES(#{productId},#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertSpec(ProductSpec s);

    @Insert("INSERT INTO product_spec_options(spec_id,value) VALUES(#{specId},#{value})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertOption(SpecOption o);

    @Delete("DELETE FROM product_spec_options WHERE spec_id IN (SELECT id FROM product_specs WHERE product_id=#{productId})")
    void deleteOptionsByProduct(Long productId);

    @Delete("DELETE FROM product_specs WHERE product_id=#{productId}")
    void deleteSpecsByProduct(Long productId);
}