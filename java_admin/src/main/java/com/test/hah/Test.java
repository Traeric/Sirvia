package com.test.hah;

import com.ericjin.javadmin.annotation.Id;
import lombok.Data;

@Data
public class Test {
    @Id
    private Integer id;

    private String name;
}
