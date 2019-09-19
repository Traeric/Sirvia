package com.ericjin.sirvia.service;

import java.util.List;
import java.util.Map;

public interface UserService {
    Boolean checkLogin(String email, String password);

    Map<String, List<Map<String, String>>> getTableList();

    Boolean confirmOldPassword(String userId, String oldPassword);

    Boolean changePassword(String userId, String new_password);
}
