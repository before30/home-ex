package cc.before30.home.resilience4j.domain;

import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ABizService
 *
 * @author before30
 * @since 2019-06-07
 */

@Service(value = "ABizService")
public class ABizService implements BizService {

    private final Connector backendAConnector;

    public ABizService(@Qualifier("backendAConnector") Connector backendAConnector) {
        this.backendAConnector = backendAConnector;
    }

    @Override
    public String failure() {
        return backendAConnector.failure();
    }

    @Override
    public String success() {
        return backendAConnector.success();
    }

    @Override
    public String ignore() {
        return backendAConnector.ignoreException();
    }

    @Override
    public Try<String> methodWithRecovery() {
        return Try.of(backendAConnector::failure)
                .recover((throwable) -> recovery());
    }

    private String recovery() {
        return "Hello world from recovery";
    }
}
