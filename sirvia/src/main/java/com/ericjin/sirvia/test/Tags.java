package com.ericjin.sirvia.test;


import com.ericjin.sirvia.annotation.Id;
import com.ericjin.sirvia.annotation.OneField;

public class Tags {
    @Id
    private Integer id;

    private String name;

    @Id
    @OneField(relation_table = "article", foreign_key = "tag", relation_bean = Article.class)
    private Article article;
}
