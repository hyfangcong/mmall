package com.mmall.controller.protal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: fangcong
 * @date: 2020/2/4
 */
@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse detail(Integer productId){
        return productService.getProductDetail(productId);
    }


    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false)String keyword,
                                         @RequestParam(value = "categoryId", required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "")String orderBy){
        return productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }

}
