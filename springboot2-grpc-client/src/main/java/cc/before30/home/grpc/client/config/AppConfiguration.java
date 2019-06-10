package cc.before30.home.grpc.client.config;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.channels.Channel;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;

/**
 * AppConfiguration
 *
 * @author before30
 * @since 2019-06-09
 */

@Configuration
@Slf4j
public class AppConfiguration {
    @Bean("executor")
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
//        return ForkJoinPool.commonPool();
    }

    @Bean("scheduledExecutor")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Bean
    public CircuitBreaker circuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(1)
                .ringBufferSizeInClosedState(5)
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("main");
        circuitBreaker.getEventPublisher()
//                .onSuccess(event -> log.info("success {}", event))
//                .onError(event -> log.info("error {}", event))
                .onEvent(event -> log.info("event {}", event));
        return circuitBreaker;
    }

    @Bean
    public Bulkhead bulkhead() {
        BulkheadConfig config = BulkheadConfig.custom().maxConcurrentCalls(1).build();
        BulkheadRegistry registry = BulkheadRegistry.of(config);
        return registry.bulkhead("main");
    }

    @Bean
    public TimeLimiter timeLimiter() {
        TimeLimiterConfig config = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(1)).build();
        return TimeLimiter.of(config);
    }

    @Bean
    public ManagedChannel channel() {
        return ManagedChannelBuilder.forAddress("127.0.0.1", 6556).usePlaintext().build();
    }
}
