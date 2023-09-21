package com.solution.blog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class EmbeddedRedisConfig {

    private final RedisServer redisServer;

    private static final int DEFAULT_REDIS_PORT = 6379;

    public EmbeddedRedisConfig() {
        this.redisServer = new RedisServer(DEFAULT_REDIS_PORT);
    }

    @PostConstruct
    private void startRedis() {
        this.redisServer.start();
    }

    @PreDestroy
    private void stopRedis() {
        this.redisServer.stop();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", DEFAULT_REDIS_PORT);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());    // key 깨짐 방지
        redisTemplate.setValueSerializer(new StringRedisSerializer());  // value 깨짐 방지
        return redisTemplate;
    }
}
