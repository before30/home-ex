package cc.before30.home.grpc.client.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * GreeterWithCircuitBreakerService
 *
 * @author before30
 * @since 2019-06-11
 */

@Slf4j
@Component
public class GreeterWithCircuitBreakerService {
    private final Executor executor;
    private final CircuitBreaker circuitBreaker;
    private final ManagedChannel channel;
    private final Bulkhead bulkhead;
    private final TimeLimiter timeLimiter;
    private final ScheduledExecutorService scheduledExecutorService;

    public GreeterWithCircuitBreakerService(Executor executor,
                                            CircuitBreaker circuitBreaker,
                                            ManagedChannel channel,
                                            Bulkhead bulkhead,
                                            TimeLimiter timeLimiter) {
        this.executor = executor;
        this.circuitBreaker = circuitBreaker;
        this.channel = channel;
        this.bulkhead = bulkhead;
        this.timeLimiter = timeLimiter;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public ListenableFuture<GreeterOuterClass.HelloReply> greetAsFuture(String name) {
        final GreeterGrpc.GreeterFutureStub stub = GreeterGrpc.newFutureStub(channel);
        final GreeterOuterClass.HelloRequest helloRequest = GreeterOuterClass
                .HelloRequest
                .newBuilder()
                .setName(name)
                .build();
        ListenableFuture<GreeterOuterClass.HelloReply> reply = stub.sayHello(helloRequest);
        return reply;
    }

    public ListenableFuture<GreeterOuterClass.HelloReply> decorateGreet(String name) {
        return Decorators.ofSupplier(() -> greetAsFuture(name))
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead)
                .get();
    }


    public void greets(List<String> names) throws ExecutionException, InterruptedException {
        for (String name : names) {
            ListenableFuture<GreeterOuterClass.HelloReply> replyFuture = Futures.withTimeout(decorateGreet(name), 10, TimeUnit.SECONDS, scheduledExecutorService);
            Futures.addCallback(replyFuture,
                    new FutureCallback<GreeterOuterClass.HelloReply>() {
                        @Override
                        public void onSuccess(@NullableDecl GreeterOuterClass.HelloReply reply) {
                            if (reply == null) {
                                log.info("reply is null.");
                            } else {
                                log.info("reply is {}.", reply.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            log.info("Failure {}", t.getMessage());
                        }
                    }, executor);
        }
    }

}
