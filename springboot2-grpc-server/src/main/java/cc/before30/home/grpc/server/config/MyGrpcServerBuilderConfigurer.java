package cc.before30.home.grpc.server.config;

import brave.Tracing;
import brave.context.log4j2.ThreadContextScopeDecorator;
import brave.grpc.GrpcTracing;
import brave.propagation.StrictScopeDecorator;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import io.grpc.ServerBuilder;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.stereotype.Component;
import zipkin2.Span;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * MyGrpcServerBuilderConfigurer
 *
 * @author before30
 * @since 2019-06-09
 */

@Component
public class MyGrpcServerBuilderConfigurer extends GRpcServerBuilderConfigurer {

    private final Executor executor;

    public MyGrpcServerBuilderConfigurer(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void configure(ServerBuilder<?> serverBuilder) {
//        BlockingQueue<Span> spans = new LinkedBlockingQueue<>();
//
//        GrpcTracing grpcTracing = GrpcTracing.create(Tracing.newBuilder()
//                .spanReporter(spans::add)
//                .currentTraceContext( // connect to log4j
//                        ThreadLocalCurrentTraceContext.newBuilder()
//                                .addScopeDecorator(StrictScopeDecorator.create())
//                                .addScopeDecorator(ThreadContextScopeDecorator.create())
//                                .build())
//                .sampler(Sampler.ALWAYS_SAMPLE).build());


        serverBuilder
                .executor(executor);
    }
}
