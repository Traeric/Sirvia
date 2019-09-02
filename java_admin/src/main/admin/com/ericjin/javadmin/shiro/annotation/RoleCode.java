package com.ericjin.javadmin.shiro.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RoleCode {
    int code() default 0;    // 默认是管理员
}
