package com.mmall.dao;

import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    List<Shipping> selectAll();

    int updateByPrimaryKey(Shipping record);

    int updateByPrimaryKeySelective(Shipping shipping);

    int deleteByPrimaryKeyAndUserId(@Param("primaryKey") Integer primaryKey, @Param("userId") Integer userId);

    Shipping selectByShippingIdAndUserId(@Param("shippingId") Integer ShippingId, @Param("userId") Integer userId);

    List<Shipping> selectByUserId(@Param("userId") Integer userId);

}