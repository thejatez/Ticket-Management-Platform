package com.ticketplatform.service.impl;

import com.ticketplatform.service.CacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl implements CacheService {

    private final Optional<RedisTemplate<String, Object>> redis;
    private final ConcurrentHashMap<String, Object> fallbackCache;

    public CacheServiceImpl(Optional<RedisTemplate<String, Object>> redis) {
        this.redis = redis;
        this.fallbackCache = new ConcurrentHashMap<>();
    }

    @Override
    public Object get(String key) {
        if (redis.isPresent()) {
            try {
                return redis.get().opsForValue().get(key);
            } catch (Exception e) {
                return fallbackCache.get(key);
            }
        }
        return fallbackCache.get(key);
    }

    @Override
    public void put(String key, Object value) {
        if (redis.isPresent()) {
            try {
                redis.get().opsForValue().set(key, value, 10, TimeUnit.MINUTES);
            } catch (Exception e) {
            }
        }
        fallbackCache.put(key, value);
    }

    @Override
    public void evict(String key) {
        if (redis.isPresent()) {
            try {
                redis.get().delete(key);
            } catch (Exception e) {
            }
        }
        fallbackCache.remove(key);
    }
}
