package cc.before30.home.resilience4j.domain;

import cc.before30.home.resilience4j.exception.BizException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

/**
 * BackendAConnector
 *
 * @author before30
 * @since 2019-06-06
 */

//@CircuitBreaker(name = "backendA")
@Component(value = "backendAConnector")
public class BackendAConnector implements Connector {
    private final CircuitBreaker circuitBreaker;

    public BackendAConnector(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public String failure() {

        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
    }

    @Override
    public String ignoreException() {
        throw new BizException("This exception is ignored by the CircuitBreaker of backend A");
    }

    @Override
    public String success() {
        return "Hello World from backend A";
    }
}
