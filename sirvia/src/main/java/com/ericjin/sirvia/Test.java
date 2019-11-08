package com.ericjin.sirvia;

import com.ericjin.sirvia.generate.Generate;

public class Test {
    public static void main(String[] args) {
        new Generate().createUser("com.mysql.jdbc.Driver",
                "jdbc:mysql://127.0.0.1:3306/java_admin?serverTimezone=UTC", "root", "root");
    }
}
