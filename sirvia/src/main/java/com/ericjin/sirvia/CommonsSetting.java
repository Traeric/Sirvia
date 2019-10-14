package com.ericjin.sirvia;

import lombok.Data;

import java.util.Map;

/**
 * 单例模式
 */
@Data
public class CommonsSetting {
    private Map<String, String> dataBase;

    private Map<String, String> redisBase;


    /**
     * action类的位置
     *
     * @return
     */
    public String actionPackage() {
        return "com.ericjin.sirvia.Action";
    }

    public Map<String, String> getRedisBase() {
        return redisBase;
    }

    /**
     * 单例相关
     */
//    private static class SingletonInstance {
//        private static final CommonsSetting instance = new CommonsSetting();
//    }
//
//    // 私有化构造器
//    private CommonsSetting() {}
//
//    // 防止反序列化
//    private Object readResolve() throws ObjectStreamException {
//        return SingletonInstance.instance;
//    }
//
//    public static CommonsSetting getInstance() {
//        return SingletonInstance.instance;
//    }
}
