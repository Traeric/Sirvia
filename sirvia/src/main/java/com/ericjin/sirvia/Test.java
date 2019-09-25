package com.ericjin.sirvia;

import redis.clients.jedis.Jedis;

public class Test {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.188.128", 6379);
        System.out.println(jedis.type("fff"));
    }
}
