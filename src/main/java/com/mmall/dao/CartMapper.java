package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    Cart selectByPrimaryKey(Integer id);

    List<Cart> selectAll();

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectByUserId(Integer userId);

    int updateByByPrimaryKeySelective(Cart cart);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteProducts(@Param("userId") Integer userId, @Param("productIdList")List<String> productIdList);

    int checkedOrUnchecked(@Param("userId")Integer userId, @Param("productId")Integer productId, @Param("checked")Integer checked);

    int selectCartProductCount(@Param("userId")Integer userId);
}