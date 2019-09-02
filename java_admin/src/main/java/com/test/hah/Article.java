package com.test.hah;

import com.ericjin.javadmin.annotation.Id;

public class Article {
    @Id
    private Integer id;
    private String name;
    private StringBuilder content;
}
