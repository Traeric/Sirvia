package com.ericjin.javadmin.mapper;


import com.ericjin.javadmin.beans.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    void createUserTable();

    Boolean createUser(User user);

    User checkUser(@Param("email") String email);
}
