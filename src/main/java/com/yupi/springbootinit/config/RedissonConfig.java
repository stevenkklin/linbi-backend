package com.yupi.springbootinit.config;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Steven
 * @create 2023-07-06-16:27
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private Integer database;

    private String host;

    private Integer port;

    private  String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(database)
                .setAddress("redis://"+ host +":" + port)
                .setPassword("abc123456");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
