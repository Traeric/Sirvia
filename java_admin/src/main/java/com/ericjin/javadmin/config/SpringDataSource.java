package com.ericjin.javadmin.config;

import com.github.pagehelper.PageInterceptor;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

@Configuration
public class SpringDataSource {
    @Value("#{commonsSetting.dataBase.get('url')}")
    private String jdbcUrl;

    @Value("#{commonsSetting.dataBase.get('user')}")
    private String jdbcUsername;

    @Value("#{commonsSetting.dataBase.get('password')}")
    private String jdbcPassword;

    @Value("#{commonsSetting.dataBase.get('driver')}")
    private String jdbcDriverClass;


    /**
     * MyBatis 数据连接地址池配置
     *
     * @throws SQLException
     */
    @Bean(name = "comboPooledDataSource", destroyMethod = "close")
    public ComboPooledDataSource comboPooledDataSource() throws SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        try {
            dataSource.setDriverClass(jdbcDriverClass);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        // 配置初始化大小、最小、最大
        dataSource.setAcquireIncrement(5);
        dataSource.setInitialPoolSize(10);
        dataSource.setMinPoolSize(5);
        dataSource.setMaxPoolSize(20);
        return dataSource;
    }

    /**
     * MyBatis配置 :配置sqlSessionFactory
     *
     * @return
     * @throws Exception
     */
    @Bean("sqlSessionFactoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean(ComboPooledDataSource comboPooledDataSource) throws Exception {
        // 分页
        PageInterceptor interceptor = new PageInterceptor();
        // 创建SqlSession工厂
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{interceptor});
        sqlSessionFactoryBean.setDataSource(comboPooledDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/*Mapper.xml"));
        return sqlSessionFactoryBean;
    }

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        mapperScannerConfigurer.setBasePackage("com.ericjin.javadmin.mapper");
        return mapperScannerConfigurer;
    }
}
