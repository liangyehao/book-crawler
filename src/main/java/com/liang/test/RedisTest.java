package com.liang.test;

import redis.clients.jedis.Jedis;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/3/16 15:06
 * @content
 */
public class RedisTest {

    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping());
    }
}
