package com.ericjin.sirvia.service.impl;

import com.ericjin.sirvia.Settings;
import com.ericjin.sirvia.annotation.ShowName;
import com.ericjin.sirvia.mapper.UserMapper;
import com.ericjin.sirvia.service.UserService;
import com.ericjin.sirvia.shiro.enumerate.RoleType;
import com.ericjin.sirvia.shiro.token.UserToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource(name = "systemTableList")
    private List<Class> systemTableList;

    @Resource(name = "userTableList")
    private List<Class> userTableList;

    @Autowired
    private UserMapper userMapper;

    /**
     * 验证用户是否登陆
     *
     * @param email
     * @param password
     * @return
     */
    @Override
    public Boolean checkLogin(String email, String password) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            // 没有通过认证
            UserToken token = new UserToken(email, password, RoleType.Admin);
            token.setRememberMe(true);
            try {
                subject.login(token);
                return true;
            } catch (AuthenticationException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 读取所有的表名
     *
     * @return
     */
    @Override
    public Map<String, List<Map<String, String>>> getTableList() {
        List<Map<String, String>> systemTableName = new ArrayList<>();
        List<Map<String, String>> userTableName = new ArrayList<>();
        // 读取系统表
        systemTableList.parallelStream().forEach((table) -> systemTableName.add(this.getTableMap(table)));
        // 读取用户表
        userTableList.parallelStream().forEach((table) -> userTableName.add(this.getTableMap(table)));
        Map<String, List<Map<String, String>>> result = new LinkedHashMap<>();
        result.put("systemTable", systemTableName);
        result.put("userTable", userTableName);
        return result;
    }


    /**
     * 构建单个表信息
     *
     * @param table
     * @return
     */
    private Map<String, String> getTableMap(Class table) {
        Map<String, String> map = new LinkedHashMap<>();
        // 是否标注了ShowName注解
        if (table.isAnnotationPresent(ShowName.class)) {
            // 标注了，拿到想要显示的表名
            map.put("table", ((ShowName) table.getAnnotation(ShowName.class)).name());
        } else {
            // 没有标注，直接使用对象名
            map.put("table", table.getSimpleName());
        }
        // 添加url
        map.put("url", table.getSimpleName());
        return map;
    }

    /**
     * 验证旧密码
     *
     * @param userId
     * @param oldPassword
     * @return
     */
    @Override
    public Boolean confirmOldPassword(String userId, String oldPassword) {
        String password = userMapper.getUser(userId);
        // 检查密码
        oldPassword = new SimpleHash("MD5", oldPassword, ByteSource.Util.bytes(Settings.salt), 1024).toString();
        return oldPassword.equals(password);
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param new_password
     * @return
     */
    @Override
    public Boolean changePassword(String userId, String new_password) {
        // 密码加密
        new_password = new SimpleHash("MD5", new_password, ByteSource.Util.bytes(Settings.salt), 1024).toString();
        return userMapper.changePassword(userId, new_password);
    }


}
