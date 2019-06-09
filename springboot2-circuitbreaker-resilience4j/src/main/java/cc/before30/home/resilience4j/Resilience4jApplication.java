package cc.before30.home.resilience4j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Resilience4jApplication
 *
 * @author before30
 * @since 2019-06-06
 */

@SpringBootApplication
@Slf4j
public class Resilience4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(Resilience4jApplication.class, args);
    }

}
