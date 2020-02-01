package com.mmall.util;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

/**
 * @author: fangcong
 * @date: 2020/1/31
 */
@Component
public class MD5Util {
    @Autowired
    private PropertiesUtil propertiesUtil;
    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultsb = new StringBuffer();
        for(int i = 0; i < b.length; i++){
            resultsb.append(byteToHexString(b[i]));
        }
        return resultsb.toString();
    }

    private static String byteToHexString(byte b){
        int n = b;
        if(n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 返回大写MD5
     *
     */
    private static String MD5Encoding(String origin, String charsetName){
        String resultString = null;
        try{
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if(charsetName == null || "".equals(charsetName)){
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            }else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
        }catch (Exception e){

        }
        return resultString.toUpperCase();
    }

    public static String MD5EncodeUtf8(String origin){
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        origin = origin + propertiesUtil.getPasswordSalt();
        return MD5Encoding(origin, "utf-8");
    }

    private static final String hexDigits[] = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
    };
}
