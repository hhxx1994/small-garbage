package com.hhx.house.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author hhx
 * @since 2018/2/18 10:32
 */
@Configuration
public class CacheConfig {

//    @Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(Collections.singletonList(new ConcurrentMapCache("models")));
//        return cacheManager;
//    }
}
