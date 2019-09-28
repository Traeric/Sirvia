package com.ericjin.sirvia;

import org.quartz.SimpleTrigger;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
//        try (Connection connection = new Connection("192.168.188.128")) {
//            Method method = Connection.class.getDeclaredMethod("sendCommand", Protocol.Command.class, String[].class);
//            method.setAccessible(true);
//            method.invoke(connection, Protocol.Command.KEYS, new String[]{"*"});
//            String re = String.valueOf(connection.getOne());
//            System.out.println(re);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        for (Protocol.Command e : Protocol.Command.values()) {
//            System.out.println(e);
//        }
        Jedis jedis = new Jedis("192.168.188.128", 6379);
        System.out.println(jedis.get("name"));
        jedis.lset("list", 1, "sssseeeettt");
    }
}
