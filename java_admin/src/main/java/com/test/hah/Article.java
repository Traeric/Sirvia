package com.test.hah;

import com.ericjin.javadmin.annotation.ForeignKey;
import com.ericjin.javadmin.annotation.Id;
import com.ericjin.javadmin.annotation.ManyToManyField;

import java.util.List;


public class Article {
    @Id
    private Integer id;
    private String name;
    private StringBuilder content;

    @ForeignKey(relation_table = "tags", relation_key = "id", show_field = "name")
    private Integer tag;

    @ManyToManyField(relation_table = "tags", third_table = "tag_article", show_field = "name", relation_field = "id",
            third_relation_field = "tag_id", third_self_field = "article_id")
    private List test;

//    @ManyToManyField(relation_table = "cate", third_table = "cate_article", show_field = "name", third_relation_field = "cate_id",
//            third_self_field = "article_id")
//    private List cate;
}
