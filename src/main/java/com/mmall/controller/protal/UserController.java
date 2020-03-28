package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.impl.UserServiceImpl;
import com.mmall.util.CookieUtil;
import com.mmall.util.JasonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: fangcong
 * @date: 2020/1/31
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session,
                                       HttpServletResponse httpServletResponse){
        ServerResponse<User> response = userServiceImpl.login(username, password);
        if(response.isSuccess()){
            CookieUtil.writeLoginToken(httpServletResponse, session.getId());
            RedisPoolUtil.setex(session.getId(), JasonUtil.obj2String(response.getData()),Const.Session.SESSION_EXPIRE_TIME);
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request,HttpServletResponse response){
        RedisPoolUtil.del(CookieUtil.readLoginToken(request));      //删除redis中保存的user对象
        CookieUtil.delLoginToken(response);                         //让存放sessionId的cookie立即过期
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(@RequestBody User user){
        return userServiceImpl.register(user);
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type){
        return userServiceImpl.checkValid(str, type);
    }


    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登录，无法获取用户信息");
        }
        String userJasonStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userJasonStr, User.class);
        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMsg("用户未登录，无法获取用户信息");
    }


    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return userServiceImpl.getQuestion(username);
    }


    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return userServiceImpl.checkAnswer(username, question, answer);
    }


    @RequestMapping(value = "forget_rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username, String passwdNew, String token){
        return userServiceImpl.forgetRestPassword(username, passwdNew, token);
    }

    //用户在登陆成功的状态下，修改密码。
    @RequestMapping(value = "rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> restPassword(HttpServletRequest request, String passwordOld, String passwordNew){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User user = JasonUtil.string2Obj(userStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        return userServiceImpl.restPassowrd(passwordOld, passwordNew, user);
    }

    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                                  @RequestBody User user){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User currentUser = JasonUtil.string2Obj(userStr, User.class);
        if(currentUser == null){
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        user.setId(currentUser.getId());
        ServerResponse<User> response = userServiceImpl.updateInformation(user);
        if(response.isSuccess()){
            CookieUtil.writeLoginToken(httpServletResponse, CookieUtil.readLoginToken(request));
            RedisPoolUtil.set(CookieUtil.readLoginToken(request), JasonUtil.obj2String(response.getData()));
        }
        return response;
    }

    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        String userStr = RedisPoolUtil.get(loginToken);
        User currentUser = JasonUtil.string2Obj(userStr, User.class);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登陆");
        }
        return userServiceImpl.getInformation(currentUser.getId());
    }
}
