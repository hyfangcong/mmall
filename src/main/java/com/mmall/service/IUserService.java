package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @author: fangcong
 * @date: 2020/1/31
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> getQuestion(String username);

    ServerResponse<String> forgetRestPassword(String username, String passwdNew, String forgetToken);

    ServerResponse<String> restPassowrd(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

    ServerResponse<String> checkAdminRole(User user);

}
