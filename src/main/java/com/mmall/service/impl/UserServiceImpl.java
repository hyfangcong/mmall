package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author: fangcong
 * @date: 2020/1/31
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMsg("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user == null){
            return ServerResponse.createByErrorMsg("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        if(user.getRole() == null || user.getRole() == 0)
            user.setRole(Const.Role.ROLE_CUSTOMER);
        else{
            user.setRole(Const.Role.ROLE_ADMIN);
        }
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMsg("注册失败");
        }
        return ServerResponse.createBySuccessMsg("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type){
        if(StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMsg("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMsg("邮箱已存在");
                }
            }
            return ServerResponse.createBySuccessMsg("校验成功");
        }else{
            return ServerResponse.createByErrorMsg("参数错误");
        }
    }

    public ServerResponse<String> getQuestion(String username){
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        String question = userMapper.getQuestion(username);
        if(StringUtils.isBlank(question)){
            return ServerResponse.createByErrorMsg("密码提示问题不存在");
        }
        return ServerResponse.createBySuccess(question);
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer){
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount > 0){
            //问题及答案是这个用户的，并且正确
            String forgetToken = UUID.randomUUID().toString();
            RedisPoolUtil.setex(Const.TOKEN_PREFIX + username, forgetToken, 60 * 30); //设置token的过期时间为30分钟
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMsg("问题的答案错误");
    }

    public ServerResponse<String> forgetRestPassword(String username, String passwdNew, String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMsg("参数错误，token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        String token = RedisPoolUtil.get(Const.TOKEN_PREFIX + username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMsg("token无效或者过期");
        }

        if(StringUtils.equals(forgetToken, token)){
            String md5Passwd = MD5Util.MD5EncodeUtf8(passwdNew);
            int rowCount = userMapper.updatePassword(username, md5Passwd);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMsg("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMsg("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMsg("修改密码失败");
    }

    public ServerResponse<String> restPassowrd(String passwordOld, String passwordNew, User user){
        //防止横向越权，一定要校验用户的旧密码是指定的这个用户
        int rowCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if(rowCount == 0){
            return ServerResponse.createByErrorMsg("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        rowCount = userMapper.updateByPrimaryKeySelective(user);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMsg("更新密码成功");
        }
        return ServerResponse.createByErrorMsg("更新密码失败");
    }

    public ServerResponse<User> updateInformation(User user){
        //username不能更新
        //email更新的时候要检查是否已经存在
        ServerResponse validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMsg("更新个人信息成功");
        }
        return ServerResponse.createByErrorMsg("更新个人信息失败");
    }

    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMsg("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse<String> checkAdminRole(User user){
        if(user != null && user.getRole() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
