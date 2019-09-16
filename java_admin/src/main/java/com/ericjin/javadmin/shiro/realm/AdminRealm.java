package com.ericjin.javadmin.shiro.realm;

import com.ericjin.javadmin.Settings;
import com.ericjin.javadmin.beans.User;
import com.ericjin.javadmin.mapper.UserMapper;
import com.ericjin.javadmin.shiro.annotation.RoleCode;
import com.ericjin.javadmin.shiro.token.UserToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

@RoleCode
public class AdminRealm extends AuthenticatingRealm {
    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UserToken token = (UserToken) authenticationToken;
        String email = token.getUsername();
        // 从数据库获取用户信息
        User user = userMapper.checkUser(email);
        // 加盐
        ByteSource salt = ByteSource.Util.bytes(Settings.salt);
        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
    }
}
