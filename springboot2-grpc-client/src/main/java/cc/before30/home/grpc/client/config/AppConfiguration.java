package cc.before30.home.grpc.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
