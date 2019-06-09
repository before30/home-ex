package cc.before30.home.resilience4j.exception;

/**
 * BizException
 *
 * @author before30
 * @since 2019-06-06
 */
public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }
}
