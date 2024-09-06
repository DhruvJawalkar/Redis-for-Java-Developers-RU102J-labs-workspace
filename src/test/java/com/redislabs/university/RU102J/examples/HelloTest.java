package com.redislabs.university.RU102J.examples;

import com.redislabs.university.RU102J.HostPort;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HelloTest {

    @Test
    //basic test case to store and retrieve a string (with key: "hello", value: "world") in redis using the Jedis client
    public void sayHelloBasic() {
        Jedis jedis = new Jedis(HostPort.getRedisHost(), HostPort.getRedisPort());

        if (HostPort.getRedisPassword().length() > 0) {
            jedis.auth(HostPort.getRedisPassword());
        }

        jedis.set("hello", "world");
        String value = jedis.get("hello");

        assertThat(value, is("world"));
    }

    @Test
    //basic test case to store and retrieve a string (with key: "hello", value: "world") in redis using the Jedis client
    //matching assertions on set() operation and get() result
    public void sayHello() {
        Jedis jedis = new Jedis(HostPort.getRedisHost(), HostPort.getRedisPort());

        if (HostPort.getRedisPassword().length() > 0) {
            jedis.auth(HostPort.getRedisPassword());
        }

        String result = jedis.set("hello", "world");
        assertThat(result, is("OK"));
        String value = jedis.get("hello");
        assertThat(value, is("world"));

        jedis.close();
    }

    @Test
    //basic test case to store and retrieve a string (with key: "hello", value: "world") in redis but in a thread safe manner using the JedisPool client (it uses a connection pool)
    //matching assertions on set() operation and get() result
    //.close() operation closes the TCP socket connection with redis instance
    public void sayHelloThreadSafe() {
        JedisPool jedisPool;

        String password = HostPort.getRedisPassword();

        if (password.length() > 0) {
            jedisPool = new JedisPool(new JedisPoolConfig(),
                HostPort.getRedisHost(), HostPort.getRedisPort(), 2000, password);
        } else {
            jedisPool = new JedisPool(new JedisPoolConfig(),
                HostPort.getRedisHost(), HostPort.getRedisPort());
        }

        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.set("hello", "world");
            assertThat(result, is("OK"));
            String value = jedis.get("hello");
            assertThat(value, is("world"));
        }

        jedisPool.close();
    }
}
