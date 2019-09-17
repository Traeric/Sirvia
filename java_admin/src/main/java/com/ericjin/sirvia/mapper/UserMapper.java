package com.ericjin.sirvia.mapper;


import com.ericjin.sirvia.beans.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    @Update("CREATE TABLE IF NOT EXISTS admin_user(\n" +
            "            id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "            username VARCHAR(16) NOT NULL DEFAULT 'java_admin',\n" +
            "            password VARCHAR(64) NOT NULL DEFAULT '123',\n" +
            "            email VARCHAR(32) NOT NULL DEFAULT 'javaadmin@gmail.com',\n" +
            "            is_admin CHAR(1) NOT NULL DEFAULT '0'\n" +
            "        )ENGINE=INNODB;")
    void createUserTable();

    @Insert("INSERT INTO admin_user(username, password, email, is_admin) VALUES(#{username}, #{password}, #{email}, #{isAdmin})")
    Boolean createUser(User user);

    User checkUser(@Param("email") String email);

    String getUser(@Param("id") String userId);

    Boolean changePassword(@Param("id") String userId, @Param("password") String password);
}
