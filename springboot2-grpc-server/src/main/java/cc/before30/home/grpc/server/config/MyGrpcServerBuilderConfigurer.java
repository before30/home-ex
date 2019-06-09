package cc.before30.home.grpc.server.config;

import io.grpc.ServerBuilder;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * MyGrpcServerBuilderConfigurer
 *
 * @author before30
 * @since 2019-06-09
 */

@Component
public class MyGrpcServerBuilderConfigurer extends GRpcServerBuilderConfigurer {

    private final ExecutorService executorService;

    public MyGrpcServerBuilderConfigurer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void configure(ServerBuilder<?> serverBuilder) {
        serverBuilder
                .executor(executorService);
    }
}
