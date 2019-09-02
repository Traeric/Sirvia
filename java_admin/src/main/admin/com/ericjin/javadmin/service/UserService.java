package com.ericjin.javadmin.service;

import java.util.List;
import java.util.Map;

public interface UserService {
    Boolean checkLogin(String email, String password);

    Map<String, List<Map<String, String>>> getTableList();
}
