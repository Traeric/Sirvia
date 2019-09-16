package com.ericjin.javadmin.annotation;

import java.lang.annotation.*;

/**
 * 实体类对应的表名
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityTableName {
    String name() default "";
}
