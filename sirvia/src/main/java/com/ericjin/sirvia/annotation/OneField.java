package com.ericjin.sirvia.annotation;

import java.lang.annotation.*;

/**
 * 在一对多的时候，标注在一对多的一的字段上面
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneField {
    String relation_table();   // 关联的表

    String foreign_key();     // 外键字段名

    Class relation_bean();  // 关联的表对应的javabean
}
