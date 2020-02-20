package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: fangcong
 * @date: 2020/2/20
 */
@Service
public class ShippingServiceImpl implements IShippingService{
    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新增地址成功", result);
        }
        return ServerResponse.createByErrorMsg("新增地址失败");
    }

    public ServerResponse update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByPrimaryKeySelective(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMsg("更新地址成功");
        }else{
            return ServerResponse.createByErrorMsg("更新地址失败");
        }
    }

    public ServerResponse delete(Integer userId, Integer shippingId){
        int rowCount = shippingMapper.deleteByPrimaryKeyAndUserId(shippingId, userId);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMsg("删除成功");
        }else{
            return ServerResponse.createByErrorMsg("删除失败");
        }
    }

    public ServerResponse getShipping(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(shippingId, userId);
        if(shipping == null){
            return ServerResponse.createByErrorMsg("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("查询该地址成功",shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
