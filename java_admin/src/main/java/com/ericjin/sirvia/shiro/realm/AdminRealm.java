package com.ericjin.sirvia.shiro.realm;

import com.ericjin.sirvia.Settings;
import com.ericjin.sirvia.beans.User;
import com.ericjin.sirvia.mapper.UserMapper;
import com.ericjin.sirvia.shiro.annotation.RoleCode;
import com.ericjin.sirvia.shiro.token.UserToken;
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
