package com.mmall.util;

import com.mmall.common.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: fangcong
 * @date: 2020/3/23
 */
@Slf4j
public class CookieUtil {
    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(Const.Cookie.COOKIE_NAME, token);
        cookie.setDomain(Const.Cookie.COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        //单位是秒
        cookie.setMaxAge(60 * 60 * 24 * 30);
        //log.info("write cookie name {}, cookie value{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie c : cookies){
                if(StringUtils.equals(Const.Cookie.COOKIE_NAME, c.getName())){
                  //  log.info("read cookie name{}, cookie value{}", c.getName(), c.getValue());
                    return c.getValue();
                }
            }
        }
        return null;
    }

    public static void delLoginToken(HttpServletResponse response){
        Cookie cookie = new Cookie(Const.Cookie.COOKIE_NAME,"");
        cookie.setDomain(Const.Cookie.COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        //单位是秒
        cookie.setMaxAge(0);   //设置为零，cookie立马过期
        response.addCookie(cookie);
        //log.info("del cookie name{}, cookie value{}", cookie.getName(), cookie.getValue());
    }
}
