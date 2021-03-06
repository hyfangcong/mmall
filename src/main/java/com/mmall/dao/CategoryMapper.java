package com.mmall.dao;

import com.mmall.pojo.Category;
import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    Category selectByPrimaryKey(Integer id);

    List<Category> selectAll();

    int updateByPrimaryKey(Category record);

    int updateByPrimaryKeySelective(Category category);

    List<Category> selectCategoryChildrenByParentId(Integer parentId);
}