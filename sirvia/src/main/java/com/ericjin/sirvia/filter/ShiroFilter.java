package com.ericjin.sirvia.filter;

import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.annotation.WebFilter;

/**
 * 替换掉web.xml中的配置
 */
@WebFilter(filterName = "shiroFilter", urlPatterns = "/*")
public class ShiroFilter extends DelegatingFilterProxy {
}
