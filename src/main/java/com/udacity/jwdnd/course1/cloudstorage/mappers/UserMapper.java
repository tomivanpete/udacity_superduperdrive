package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES (#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE USERS SET username = #{username}, salt = #{salt}, password = #{password}, firstname = #{firstname}, lastname = #{lastname} WHERE id = #{id}")
    void update(User user);

    @Delete("DELETE FROM USERS WHERE id = #{userId}")
    void delete(int userId);
}