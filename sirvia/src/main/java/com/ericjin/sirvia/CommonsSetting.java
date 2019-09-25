package com.ericjin.sirvia;

import lombok.Data;

import java.util.Map;

@Data
public class CommonsSetting {
    private Map<String, String> dataBase;

    private Map<String, String> redisBase;


    /**
     * action类的位置
     * @return
     */
    public String actionPackage() {
        return "com.ericjin.sirvia.Action";
    }
}
