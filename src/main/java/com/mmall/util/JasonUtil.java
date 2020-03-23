package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author: fangcong
 * @date: 2020/3/21
 */
@Slf4j
public class JasonUtil {
    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();

        //全部字段都进行序列化
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        //忽略在json中存在，但是在java对象中不存在的属性
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String obj2StringPretty(Object obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.error("serialization error: ", e);
            return null;
        }
    }

    public static String obj2String(Object obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("serialization error: ", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<T> clazz){
        if(StringUtils.isEmpty(str)){
            return null;
        }
        try {
            return objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.error("deserialization error: ", e);
            return null;
        }
    }


    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str)){
            return null;
        }
        try {
            return objectMapper.readValue(str, typeReference);
        } catch (IOException e) {
            log.error("deserialization error: ", e);
            return null;
        }
    }


    public static <T> T string2Obj(String str, Class<?> collection, Class<?>... elementClasses){
        if(StringUtils.isEmpty(str)){
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collection, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.error("deserialization error: ", e);
            return null;
        }
    }
}
