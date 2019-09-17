package com.ericjin.sirvia.annotation;

import java.lang.annotation.*;

/**
 * 在java admin上面显示的表的名字
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ShowName {
    String name() default "TableName";
}
