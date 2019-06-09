package cc.before30.home.resilience4j.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfiguration
 *
 * @author before30
 * @since 2019-06-06
 */

@Configuration
public class AppConfiguration {

    @Bean
    public CircuitBreaker circuitBreaker() {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backendName");
        return circuitBreaker;
    }
}
