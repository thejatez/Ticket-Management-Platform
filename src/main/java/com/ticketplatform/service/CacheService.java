package com.ticketplatform.service;

public interface CacheService {

    Object get(String key);

    void put(String key, Object value);

    void evict(String key);
}
