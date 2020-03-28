package com.mmall.controller.backend;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.impl.CategoryServiceImpl;
import com.mmall.service.impl.UserServiceImpl;
import com.mmall.util.CookieUtil;
import com.mmall.util.JasonUtil;
import com.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: fangcong
 * @date: 2020/2/1
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpServletRequest request, String categoryName,
                                              @RequestParam(value = "parentId", defaultValue = "0") int parentId){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        //校验是否是管理员
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //增加分类管理的逻辑
            return categoryService.addCategory(categoryName, parentId);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }


    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setCategory(HttpServletRequest request, Integer categoryId, String categoryName){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        //校验是否是管理员
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //更新categoryName
            return categoryService.setCategory(categoryId, categoryName);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }


    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0")Integer categoryId){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        //校验是否是管理员
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //查询子节点的category信息，并且不递归，保持平级
            return categoryService.getChildrenParallelCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }


    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0")Integer categoryId){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        //校验是否是管理员
        if(userService.checkAdminRole(user).isSuccess()){
            //是管理员
            //查询当前节点的id以及递归子节点的id
            return categoryService.selectCategoryAndChildrenById(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

}
