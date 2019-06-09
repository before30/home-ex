package cc.before30.home.resilience4j.domain;


/**
 * Connector
 *
 * @author before30
 * @since 2019-06-06
 */
public interface Connector {
    String failure();

    String success();

    String ignoreException();
}
