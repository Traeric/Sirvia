package com.ericjin.javadmin.mapper;


import com.ericjin.javadmin.beans.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    void createUserTable();

    Boolean createUser(User user);

    User checkUser(@Param("email") String email);

    String getUser(@Param("id") String userId);

    Boolean changePassword(@Param("id") String userId, @Param("password") String password);
}
