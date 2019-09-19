package com.ericjin.sirvia.test;


import com.ericjin.sirvia.annotation.ForeignKey;
import com.ericjin.sirvia.annotation.Id;
import com.ericjin.sirvia.annotation.ManyToManyField;
import com.ericjin.sirvia.annotation.ShowName;

import java.util.List;


@ShowName(name = "文章表")
public class Article {
    @Id
    private Integer id;
    private String name;
    private StringBuilder content;

    @ForeignKey(relation_table = "tags", relation_key = "id", show_field = "name", relation_bean = Tags.class)
    private Integer tag;

    @ManyToManyField(relation_table = "tags", third_table = "tag_article", show_field = "name", relation_field = "id",
            third_relation_field = "tag_id", third_self_field = "article_id", relation_bean = Tags.class)
    private List tags;

//    @ManyToManyField(relation_table = "cate", third_table = "cate_article", show_field = "name", third_relation_field = "cate_id",
//            third_self_field = "article_id")
//    private List cate;
}
