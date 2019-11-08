package com.ericjin.sirvia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc   // 启动SpringMvc
@ComponentScan("com.ericjin.sirvia.controller")
public class WebConfig extends WebMvcConfigurerAdapter {
    /**
     * 配置静态资源处理器
     *
     * @param configurer DefaultServletHandlerConfigurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();        //对静态资源的请求转发到容器缺省的servlet，而不使用DispatcherServlet
    }
}
