package com.solution.blog.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;

@Configuration
@EnableCaching
@EnableScheduling
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(
                Arrays.asList(
                        new ConcurrentMapCache("blogStore")
                )
        );
        return simpleCacheManager;
    }

    @CacheEvict(value = "blogStore", allEntries = true)
    @Scheduled(fixedDelay = 60 * 1000) // 60초
    public void emptyDaumStore() {
        System.out.println("remove blogStore cache");
    }
}
