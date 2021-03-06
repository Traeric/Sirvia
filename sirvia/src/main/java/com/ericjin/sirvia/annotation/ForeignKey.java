package com.ericjin.sirvia.annotation;

import java.lang.annotation.*;

/**
 * 标注在外键字段上
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {
    String relation_table();   // 关联的表

    String relation_key() default "id";     // 关联字段

    String show_field();     // 要展示的字段

    Class relation_bean();   // 关联的java bean对象
}
