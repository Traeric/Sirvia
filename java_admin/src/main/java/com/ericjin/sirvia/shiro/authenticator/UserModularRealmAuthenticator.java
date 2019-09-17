package com.ericjin.sirvia.shiro.authenticator;

import com.ericjin.sirvia.shiro.enumerate.RoleType;
import com.ericjin.sirvia.shiro.token.UserToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {
    /**
     * 重写doAuthenticate方法
     * 在特定情况下指定特定的realm进行验证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 判断getRealms()是否为空
        assertRealmsConfigured();
        // 将token转换成UserToken
        UserToken token = (UserToken) authenticationToken;
        // 获取角色类型
        RoleType roleType = token.getRoleType();
        // 获取所有的realm
        Collection<Realm> realms = getRealms();
        // 筛选出需要执行的realm
        List<Realm> collect = realms.parallelStream().filter(roleType::getExecuteRealm).collect(Collectors.toList());
        // 判断是单realm还是多realm，分别执行不同的方法
        return collect.size() == 1
                ? this.doSingleRealmAuthentication(collect.iterator().next(), token)
                : this.doMultiRealmAuthentication(collect, token);
    }
}
