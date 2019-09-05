package com.test.hah;

import com.ericjin.javadmin.annotation.ForeignKey;
import com.ericjin.javadmin.annotation.Id;

public class Article {
    @Id
    private Integer id;
    private String name;
    private StringBuilder content;

    @ForeignKey(relation_table = "tags", relation_key = "id", show_field = "name")
    private Integer tag;
}
