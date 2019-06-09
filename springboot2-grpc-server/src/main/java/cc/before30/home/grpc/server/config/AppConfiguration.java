package cc.before30.home.grpc.server.config;

import cc.before30.home.grpc.server.service.CalculatorService;
import cc.before30.home.grpc.server.service.GreetingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfiguration
 *
 * @author before30
 * @since 2019-06-09
 */

@Configuration
public class AppConfiguration {

    @Bean
    public GreetingService greeterService() {
        return new GreetingService();
    }

    @Bean
    public CalculatorService calculatorService() {
        return new CalculatorService();
    }

}
