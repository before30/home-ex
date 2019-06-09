package cc.before30.home.resilience4j.domain;

import io.vavr.control.Try;

/**
 * BizService
 *
 * @author before30
 * @since 2019-06-07
 */
public interface BizService {
    String failure();

    String success();

    String ignore();

    Try<String> methodWithRecovery();
}
