package com.ericjin.javadmin.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Choose {
    int type() default 0;   // 选择复选框的类型，是radio还是select
    String[] valueList();   // input框的值
    String[] textList();    // 显示的值
}
