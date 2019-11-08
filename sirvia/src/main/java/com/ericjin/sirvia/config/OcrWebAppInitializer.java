package com.ericjin.sirvia.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class OcrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {SpringConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {WebConfig.class};   // 执行web配置类
    }

    // 将DispatcherServlet映射到"/"
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
