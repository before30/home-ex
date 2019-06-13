package cc.before30.home.grpc.client.config;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

import java.nio.channels.Channel;
import java.time.Duration;
import java.util.concurrent.*;

/**
 * AppConfiguration
 *
 * @author before30
 * @since 2019-06-09
 */

@Configuration
@Slf4j
public class AppConfiguration {

    @Bean
    public Executor executor(ApplicationContext context) {
        BeanFactory beanFactory = context.getParentBeanFactory();
        return new LazyTraceExecutor(beanFactory, ForkJoinPool.commonPool());
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
    public ManagedChannel channel(ClientInterceptor interceptor) {
        return ManagedChannelBuilder
                .forAddress("127.0.0.1", 6556)
                .usePlaintext()
                .intercept(interceptor)
                .build();
    }

//    @Bean
//    public GrpcTracing grpcTracing(Tracing tracing) {
//        return GrpcTracing.create(tracing);
//    }

    @Bean
    ClientInterceptor grpcClientSleuthInterceptor(GrpcTracing grpcTracing) {
        return grpcTracing.newClientInterceptor();
    }

    @Bean
    public Reporter<Span> spanReporter() {
        return new Reporter<Span>() {
            @Override
            public void report(Span span) {
                log.info("{}", span.toString());
            }
        };
    }
}
