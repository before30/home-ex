package cc.before30.home.grpc.client.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
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
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduledExecutor;
    private final CircuitBreaker circuitBreaker;
    private final ManagedChannel channel;
    private final Bulkhead bulkhead;
    private final TimeLimiter timeLimiter;

    public GreeterWithCircuitBreakerService(@Qualifier("executor") ExecutorService executor,
                                            @Qualifier("scheduledExecutor") ScheduledExecutorService scheduledExecutor,
                                            CircuitBreaker circuitBreaker,
                                            ManagedChannel channel,
                                            Bulkhead bulkhead,
                                            TimeLimiter timeLimiter) {
        this.executor = executor;
        this.scheduledExecutor = scheduledExecutor;
        this.circuitBreaker = circuitBreaker;
        this.channel = channel;
        this.bulkhead = bulkhead;
        this.timeLimiter = timeLimiter;
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
        Function<String, ListenableFuture<GreeterOuterClass.HelloReply>> func = CircuitBreaker.decorateFunction(circuitBreaker, n -> greetAsFuture(n));
        Function<String, ListenableFuture<GreeterOuterClass.HelloReply>> func2 = Bulkhead.decorateFunction(bulkhead, func);

        return func2.apply(name);
    }


    public void greets(List<String> names) throws ExecutionException, InterruptedException {
        List<ListenableFuture<GreeterOuterClass.HelloReply>> futures = new ArrayList<>();
        for (String name : names) {
            ListenableFuture<GreeterOuterClass.HelloReply> replyFuture = Futures.withTimeout(decorateGreet(name), 10, TimeUnit.SECONDS, scheduledExecutor);
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
