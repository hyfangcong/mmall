package com.mmall.dao;

import com.mmall.pojo.PayInfo;
import java.util.List;

public interface PayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    List<PayInfo> selectAll();

    int updateByPrimaryKey(PayInfo record);
}