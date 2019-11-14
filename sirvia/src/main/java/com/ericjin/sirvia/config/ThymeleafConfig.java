package com.ericjin.sirvia.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class ThymeleafConfig {
    /**
     * 模板解析器
     *
     * @return ITemplateResolver
     */
    @Bean
    public ITemplateResolver templateResolver() throws IOException {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("classpath:/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    /**
     * 配置shiro方言
     *
     * @return ShiroDialect
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * 模板引擎
     *
     * @param templateResolver ITemplateResolver
     * @return TemplateEngine
     */
    @Bean
    public TemplateEngine templateEngine(ITemplateResolver templateResolver, ShiroDialect shiroDialect) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        Set<IDialect> dialectSet = new HashSet<>();
        dialectSet.add(shiroDialect);
        templateEngine.setAdditionalDialects(dialectSet);
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(TemplateEngine templateEngine) {
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine);
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        return thymeleafViewResolver;
    }
}
