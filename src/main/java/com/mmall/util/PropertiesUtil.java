package com.mmall.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: fangcong
 * @date: 2020/2/1
 */
@Component
@PropertySource(value = {"classpath:/mmall.properties"})
public class PropertiesUtil {
    @Value("${password.salt}")
    private String passwordSalt;

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
}
