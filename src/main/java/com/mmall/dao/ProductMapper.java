package com.mmall.dao;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    Product selectByPrimaryKey(Integer id);

    List<Product> selectAll();

    int updateByPrimaryKey(Product record);

    int updateByPrimaryKeySelective(Product product);

    List<Product> selectList();

    List<Product> selectByNameAndProductId(@Param("productName")String productName, @Param("productId")Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param("productName")String productName, @Param("categoryList")List<Integer> categoryList);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum,
                                                         Integer pageSize, String orderBy);
}