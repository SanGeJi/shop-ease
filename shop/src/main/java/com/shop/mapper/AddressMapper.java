package com.shop.mapper;

import com.shop.entity.Address;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressMapper {
    @Select("SELECT * FROM addresses WHERE user_id=#{userId} ORDER BY is_default DESC, created_at DESC")
    List<Address> findByUser(Long userId);

    @Select("SELECT * FROM addresses WHERE id=#{id} AND user_id=#{userId}")
    Address findById(@Param("id") Long id, @Param("userId") Long userId);

    @Insert("INSERT INTO addresses(user_id,receiver,phone,address,is_default) VALUES(#{userId},#{receiver},#{phone},#{address},#{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Address a);

    @Update("UPDATE addresses SET receiver=#{receiver},phone=#{phone},address=#{address},is_default=#{isDefault} WHERE id=#{id} AND user_id=#{userId}")
    void update(Address a);

    @Delete("DELETE FROM addresses WHERE id=#{id} AND user_id=#{userId}")
    void delete(@Param("id") Long id, @Param("userId") Long userId);

    @Update("UPDATE addresses SET is_default=0 WHERE user_id=#{userId}")
    void clearDefault(Long userId);

    @Select("SELECT * FROM addresses WHERE user_id=#{userId} AND is_default=1 LIMIT 1")
    Address findDefault(Long userId);
}
