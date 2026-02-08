package com.ticketplatform.service;

public interface RateLimitService {

    boolean allowRequest(String clientKey);
}
