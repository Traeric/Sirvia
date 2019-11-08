package com.ericjin.sirvia.config;

import com.ericjin.sirvia.CommonsSetting;
import com.ericjin.sirvia.Register;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan(basePackages={"com.ericjin.sirvia"},
        excludeFilters = { @ComponentScan.Filter(type= FilterType.ANNOTATION,value= EnableWebMvc.class)})
@Import({ThymeleafConfig.class, SpringDataSource.class, ShiroConfig.class})
public class SpringConfig {
    /**
     * 配置jedis
     *
     * @return Jedis
     */
    @Bean
    public Jedis jedis() {
        return new Jedis(commonsSetting().getRedisBase().get("host"),
                Integer.parseInt(commonsSetting().getRedisBase().get("port")));
    }

    /**
     * 配置注册的表信息
     *
     * @return Register
     */
    public Register register() {
        return new Register();
    }

    // 系统表
    @Bean
    public List<Class> systemTableList() {
        return register().systemTableList();
    }

    // 用户表
    @Bean
    public List<Class> userTableList() {
        return register().userTableList();
    }

    // 配置action
    @Bean
    public Map<String, String> actionMap() {
        return register().actionMap();
    }

    /**
     * 用于和外界的web项目进行沟通的类
     *
     * @return CommonsSetting
     */
    @Bean
    public CommonsSetting commonsSetting() {
        CommonsSetting commonsSetting = new CommonsSetting();
        // 数据库信息
        Map<String, String> databaseMap = new HashMap<>();
        databaseMap.put("url", "jdbc:mysql://127.0.0.1:3306/java_admin?serverTimezone=UTC");
        databaseMap.put("password", "root");
        databaseMap.put("user", "root");
        databaseMap.put("driver", "com.mysql.jdbc.Driver");
        commonsSetting.setDataBase(databaseMap);
        // redis初始信息
        Map<String, String> redisMap = new HashMap<>();
        redisMap.put("host", "119.3.146.100");
        redisMap.put("port", "6379");
        redisMap.put("passowrd", "");
        redisMap.put("database", "0");
        commonsSetting.setRedisBase(redisMap);
        return commonsSetting;
    }
}
