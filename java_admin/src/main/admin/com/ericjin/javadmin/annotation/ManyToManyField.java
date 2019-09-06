package com.ericjin.javadmin.annotation;

import java.lang.annotation.*;

/**
 * 标注在类上，表示该表有多对多字段
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToManyField {
    String relation_table();    // 关联表的名字

    String third_table();    // 第三张表的名字

    String show_field();    // 关联表要显示的字段

    String relation_field() default "id";    // 关联表的关联字段

    String third_relation_field();     // 关联字段在第三张表的名称

    String third_self_field();    // 当前字段在第三张表中的名称

    String insert_field() default "id";    // 当前表要插入第三张表的字段

    Class relation_bean();    // 关联表的java bean对象
}
