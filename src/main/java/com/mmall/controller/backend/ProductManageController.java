package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.impl.FileServiceImpl;
import com.mmall.service.impl.ProductServiceImpl;
import com.mmall.service.impl.UserServiceImpl;
import com.mmall.util.CookieUtil;
import com.mmall.util.JasonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author: fangcong
 * @date: 2020/2/2
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private FileServiceImpl fileService;

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addOrUpdateProduct(HttpServletRequest request, @RequestBody Product product){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //进行更新或者插入逻辑
            return productService.addOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员，没有权限");
        }
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest request, Integer productId, Integer status){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //对产品的状态进行更新
            return productService.setSaleStatus(productId, status);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员，没有权限");
        }
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpServletRequest request, Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //获取产品详情
            return productService.manageProductDetail(productId);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员，没有权限");
        }
    }


    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //获取产品详情
            return productService.getProductList(pageNum, pageSize);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员，没有权限");
        }
    }


    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSearch(HttpServletRequest request, String productName, Integer productId,
                                        @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //获取产品详情
            return productService.searchProduct(productName, productId, pageNum, pageSize);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员，没有权限");
        }
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        if(userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") +
                    PropertiesUtil.getProperty("ftp.server.http.img")+ targetFileName;
            Map<String, String> fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员，没有权限");
        }
    }


}
