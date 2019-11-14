package com.ericjin.sirvia.shiro.realm;

import com.ericjin.sirvia.Settings;
import com.ericjin.sirvia.beans.User;
import com.ericjin.sirvia.generate.Generate;
import com.ericjin.sirvia.mapper.UserMapper;
import com.ericjin.sirvia.shiro.annotation.RoleCode;
import com.ericjin.sirvia.shiro.token.UserToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

@RoleCode
public class AdminRealm extends AuthenticatingRealm {
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UserToken token = (UserToken) authenticationToken;
        String email = token.getUsername();
        // 从数据库获取用户信息
        UserMapper userMapper = Generate.getMapper();
        User user = userMapper.checkUser(email);
        // 加盐
        ByteSource salt = ByteSource.Util.bytes(Settings.salt);
        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
    }
}
