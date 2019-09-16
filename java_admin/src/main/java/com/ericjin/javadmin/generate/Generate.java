package com.ericjin.javadmin.generate;

import com.ericjin.javadmin.Settings;
import com.ericjin.javadmin.beans.User;
import com.ericjin.javadmin.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * 生成管理账号
 */
public class Generate {

    /**
     * 生成sqlSession
     *
     * @return
     */
    private SqlSession createSqlSession() {
        final String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            return sqlSessionFactory.openSession(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建user表
     */
    private void generateUserTable() {
        SqlSession sqlSession = this.createSqlSession();
        if (Objects.nonNull(sqlSession)) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.createUserTable();
            System.out.println("创建成功");
        }
        sqlSession.close();
    }


    /**
     * 创建账户
     */
    public void createUser() {
        boolean isRegister = true;
        System.out.println("\033[1;92m" + "创建JAVA ADMIN账户" + "\033[0m");
        Scanner scanner = new Scanner(System.in);
        // 输入用户名
        String username = "";
        System.out.println("\033[1;96m" + "请输入用户名：" + "\033[0m");
        while ("".equals(username.trim())) {
            username = scanner.nextLine();
            if ("".equals(username.trim().replace("\n", ""))) {
                System.out.println("\033[1;91m" + "用户名不能为空！" + "\033[0m");
                System.out.println("\033[1;96m" + "请输入用户名：" + "\033[0m");
            }
        }
        // 输入邮箱
        System.out.println("\033[1;96m" + "请输入邮箱：" + "\033[0m");
        String email = "";
        while ("".equals(email.trim())) {
            email = scanner.nextLine();
            if ("".equals(email.trim().replace("\n", ""))) {
                System.out.println("\033[1;91m" + "邮箱不能为空！" + "\033[0m");
                System.out.println("\033[1;96m" + "请输入邮箱：" + "\033[0m");
            }
        }
        // 输入密码
        System.out.println("\033[1;96m" + "请输入密码：" + "\033[0m");
        String password = "";
        while ("".equals(password.trim())) {
            password = scanner.nextLine();
            if ("".equals(password.trim().replace("\n", ""))) {
                System.out.println("\033[1;91m" + "密码不能为空！" + "\033[0m");
                System.out.println("\033[1;96m" + "请输入密码：" + "\033[0m");
            }
        }
        // 判断密码长度
        if (password.length() <= 6) {
            System.out.println("\033[1;91m" + "密码太短！确定要继续使用吗？" + "\033[0m");
            if (!"".equals(scanner.nextLine())) {
                isRegister = false;
            }
        }
        // 是否进行注册
        if (isRegister) {
            // 对密码进行加密
            password = new SimpleHash("MD5", password, ByteSource.Util.bytes(Settings.salt), 1024).toString();
            // 将数据写入数据库
            SqlSession sqlSession = this.createSqlSession();
            if (Objects.nonNull(sqlSession)) {
                UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
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
            }
            sqlSession.close();
        } else {
            System.out.println("\033[1;92m" + "密码太短，请重新注册！" + "\033[0m");
        }
    }

    public static void main(String[] args) {
//        new Generate().generateUserTable();
        new Generate().createUser();
    }
}
