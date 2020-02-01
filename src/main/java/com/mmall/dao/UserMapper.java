package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectLogin(@Param("username")String username, @Param("password")String password);

    int checkEmail(String email);

    String getQuestion(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePassword(@Param("username") String username, @Param("password") String password);

    int checkPassword(@Param("passwordOld") String passwordOld, @Param("id") int id);

    int updateByPrimaryKeySelective(User user);
}