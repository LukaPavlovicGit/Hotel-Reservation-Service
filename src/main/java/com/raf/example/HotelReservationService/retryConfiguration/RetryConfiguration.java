package com.raf.example.HotelReservationService.retryConfiguration;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RetryConfiguration {
    @Bean
    public Retry userServiceRetry() {
        RetryConfig config = RetryConfig.custom().waitDuration(Duration.ofSeconds(1)).build();
        RetryRegistry registry = RetryRegistry.of(config);
        return registry.retry("userServiceRetry");
    }
}