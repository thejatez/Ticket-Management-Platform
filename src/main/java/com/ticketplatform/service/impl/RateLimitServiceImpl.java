package com.ticketplatform.service.impl;

import com.ticketplatform.service.RateLimitService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitServiceImpl implements RateLimitService {

    private final ConcurrentHashMap<String, Integer> counter =
            new ConcurrentHashMap<>();

    @Override
    public boolean allowRequest(String key) {

        counter.merge(key, 1, Integer::sum);

        return counter.get(key) <= 100;
    }
}
