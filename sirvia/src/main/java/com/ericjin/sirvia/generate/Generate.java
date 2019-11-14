package com.ericjin.sirvia.generate;

import com.ericjin.sirvia.Settings;
import com.ericjin.sirvia.beans.User;
import com.ericjin.sirvia.config.SpringConfig;
import com.ericjin.sirvia.mapper.UserMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.sql.DataSource;
import java.util.Scanner;

/**
 * 生成管理账号
 */
public class Generate {

    /**
     * 生成sqlSession 不用配置文件
     *
     * @param driver 驱动名
     * @param url    数据库url
     * @param user   数据库用户名
     * @param pwd    数据库密码
     * @return SqlSession
     */
    private static SqlSession createSqlSession(String driver, String url, String user, String pwd) {
        // 创建连接池
        DataSource dataSource = new PooledDataSource(driver, url, user, pwd);
        // 事务
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        // 创建环境
        Environment environment = new Environment("development", transactionFactory, dataSource);
        // 创建配置
        Configuration configuration = new Configuration(environment);
        // 开启驼峰规则
        configuration.setMapUnderscoreToCamelCase(true);
        // 加入资源（Mapper接口）
        configuration.addMapper(UserMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory.openSession(true);
    }

    /**
     * 获取userMapper
     * @return UserMapper
     */
    public static UserMapper getMapper() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:adminApplicationContext.xml");
        return (UserMapper) applicationContext.getBean("userMapper");
    }

    /**
     * 创建user表
     */
    public static void generateUserTable() {
        UserMapper userMapper = Generate.getMapper();
        userMapper.createUserTable();
        System.out.println("\033[1;96m" + "*****创建成功*****" + "\033[0m");
    }


    /**
     * 创建账户
     */
    public static void createUser() {
        boolean isRegister = true;
        System.out.println("\033[1;92m" + "创建JAVA ADMIN账户" + "\033[0m");
        Scanner scanner = new Scanner(System.in);
        // 输入用户名
        String username = "";
        System.out.print("\033[1;96m" + "请输入用户名：" + "\033[0m");
        while ("".equals(username.trim())) {
            username = scanner.nextLine();
            if ("".equals(username.trim().replace("\n", ""))) {
                System.out.println("\033[1;91m" + "用户名不能为空！" + "\033[0m");
                System.out.print("\033[1;96m" + "请输入用户名：" + "\033[0m");
            }
        }
        // 输入邮箱
        System.out.print("\033[1;96m" + "请输入邮箱：" + "\033[0m");
        String email = "";
        while ("".equals(email.trim())) {
            email = scanner.nextLine();
            if ("".equals(email.trim().replace("\n", ""))) {
                System.out.println("\033[1;91m" + "邮箱不能为空！" + "\033[0m");
                System.out.print("\033[1;96m" + "请输入邮箱：" + "\033[0m");
            }
        }
        // 输入密码
        System.out.print("\033[1;96m" + "请输入密码：" + "\033[0m");
        String password = "";
        while ("".equals(password.trim())) {
            password = scanner.nextLine();
            if ("".equals(password.trim().replace("\n", ""))) {
                System.out.println("\033[1;91m" + "密码不能为空！" + "\033[0m");
                System.out.print("\033[1;96m" + "请输入密码：" + "\033[0m");
            }
        }
        // 判断密码长度
        if (password.length() <= 6) {
            System.out.print("\033[1;91m" + "密码太短！确定要继续使用吗？" + "\033[0m");
            if (!"".equals(scanner.nextLine())) {
                isRegister = false;
            }
        }
        // 是否进行注册
        if (isRegister) {
            // 对密码进行加密
            password = new SimpleHash("MD5", password, ByteSource.Util.bytes(Settings.salt), 1024).toString();
            // 将数据写入数据库
            UserMapper userMapper = Generate.getMapper();
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setIsAdmin("0");
            if (userMapper.createUser(user)) {
                System.out.println("\033[1;92m" + "创建成功！" + "\033[0m");
            } else {
                System.out.println("\033[1;91m" + "创建失败！" + "\033[0m");
            }
        } else {
            System.out.println("\033[1;92m" + "密码太短，请重新注册！" + "\033[0m");
        }
    }
}
