package com.ericjin.sirvia.config;

import com.ericjin.sirvia.shiro.authenticator.UserModularRealmAuthenticator;
import com.ericjin.sirvia.shiro.realm.AdminRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ShiroConfig {
    /**
     * 设置记住我的cookie信息
     *
     * @return 返回cookie对象
     */
    public SimpleCookie rememberMeCookie() {
        // cookie名
        SimpleCookie rememberMe = new SimpleCookie("rememberMe");
        // 设置cookie生存周期 秒为单位
        rememberMe.setMaxAge(2592000);
        rememberMe.setHttpOnly(true);
        return rememberMe;
    }

    /**
     * rememberMe管理器
     *
     * @return 返回rememberMe管理器实例对象
     */
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的秘钥，默认AES算法，秘钥长度(128 256 512位)
        cookieRememberMeManager.setCipherKey(Base64.decode("asdsa--543532%^%$&%&"));
        return cookieRememberMeManager;
    }

    /**
     * 配置管理realm的规则 -- 至少有一个认证成功
     *
     * @return 返回认证规则
     */
    public UserModularRealmAuthenticator authenticator() {
        UserModularRealmAuthenticator userModularRealmAuthenticator = new UserModularRealmAuthenticator();
        userModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return userModularRealmAuthenticator;
    }

    /**
     * HashedCredentialsMatcher类是对密码进行加密的，保证保存在数据库中的密码是密文
     * 当然在登录认证的时候也可以对表单中传入的数据进行加密
     *
     * @return hashedCredentialsMatcher
     */
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 指定加密方式
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 指定加密次数
        hashedCredentialsMatcher.setHashIterations(1024);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义的Realm
     *
     * @return realm
     */
    public AdminRealm adminRealm() {
        AdminRealm adminRealm = new AdminRealm();
        adminRealm.setAuthenticationCachingEnabled(true);
        adminRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return adminRealm;
    }

    /**
     * 配置SecurityManager!
     *
     * @return SecurityManager管理器
     */
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 将rememberMe管理器添加到securityManager中
        securityManager.setRememberMeManager(rememberMeManager());
        // 配置realm管理器规则
        securityManager.setAuthenticator(authenticator());
        // 添加自定义的realm
        securityManager.setRealm(adminRealm());
        return securityManager;
    }

    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     *
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        // 登录页面
        shiroFilterFactoryBean.setLoginUrl("/admin/login");
        // 登录后要跳转的页面
        shiroFilterFactoryBean.setSuccessUrl("/admin");
        // 认证不通过跳转的页面，提示用户未登录
        shiroFilterFactoryBean.setUnauthorizedUrl("/admin/login");

        // 拦截器
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("/admin/login", "anon");
        map.put("/static/**", "anon");
        map.put("/admin/logout", "logout");
        map.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }
}
