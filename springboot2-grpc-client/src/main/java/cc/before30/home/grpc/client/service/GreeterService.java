package cc.before30.home.grpc.client.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * GreeterService
 *
 * @author before30
 * @since 2019-06-09
 */

@Slf4j
@Component
public class GreeterService {

    private final ExecutorService executor;
    private final ScheduledExecutorService scheduledExecutor;

    public GreeterService(@Qualifier("executor") ExecutorService executor,
                          @Qualifier("scheduledExecutor") ScheduledExecutorService scheduledExecutor) {
        this.executor = executor;
        this.scheduledExecutor = scheduledExecutor;
    }

    public ListenableFuture<GreeterOuterClass.HelloReply> greetAsFuture(String name) {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 6556).usePlaintext().build();
        final GreeterGrpc.GreeterFutureStub stub = GreeterGrpc.newFutureStub(channel);
        final GreeterOuterClass.HelloRequest helloRequest =   GreeterOuterClass.HelloRequest.newBuilder().setName(name).build();

        ListenableFuture<GreeterOuterClass.HelloReply> reply = stub.sayHello(helloRequest);

        return reply;
    }

    public void greets2(List<String> names) {
        List<ListenableFuture<GreeterOuterClass.HelloReply>> futures = new ArrayList<>();
        for (String name : names) {
            ListenableFuture<GreeterOuterClass.HelloReply> future = Futures.withTimeout(greetAsFuture(name),1, TimeUnit.SECONDS, scheduledExecutor);
            futures.add(future);
            Futures.addCallback(future, new FutureCallback<GreeterOuterClass.HelloReply>() {
                @Override
                public void onSuccess(@NullableDecl GreeterOuterClass.HelloReply result) {
                    log.info("Success {}", result.getMessage());
                }

                @Override
                public void onFailure(Throwable t) {
                    log.info("Failure {}", t.getMessage());
                }
            }, executor);
        }

        ListenableFuture<List<GreeterOuterClass.HelloReply>> listListenableFuture = Futures.successfulAsList(futures);
        Futures.addCallback(listListenableFuture,
                new FutureCallback<List<GreeterOuterClass.HelloReply>>() {
                    @Override
                    public void onSuccess(@NullableDecl List<GreeterOuterClass.HelloReply> result) {
                        for (GreeterOuterClass.HelloReply reply : result) {
                            if (reply == null) {
                                log.info("reply is null");
                            } else {
                                log.info("reply is {}", reply.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        log.info("Failure {}", t.getMessage());
                    }
                }, executor);
    }

    public void greets(List<String> names) throws ExecutionException, InterruptedException {
        List<ListenableFuture<GreeterOuterClass.HelloReply>> futures = new ArrayList<>();
        for (String name : names) {
            ListenableFuture<GreeterOuterClass.HelloReply> replyFuture = Futures.withTimeout(greetAsFuture(name), 1, TimeUnit.SECONDS, scheduledExecutor);
            Futures.addCallback(replyFuture,
                new FutureCallback<GreeterOuterClass.HelloReply>() {
                    @Override
                    public void onSuccess(@NullableDecl GreeterOuterClass.HelloReply reply) {
                        if (reply == null) {
                            log.info("reply is null");
                        } else {
                            log.info("reply is {}", reply.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        log.info("Failure {}", t.getMessage());
                    }
                }, executor);
        }
    }

    public String greet(String name) throws ExecutionException, InterruptedException {

//        final List<CompletableFuture<Void>> workingJobs = new CopyOnWriteArrayList<>();
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println(""), executor);
//        workingJobs.add(future);
//        future.whenCompleteAsync((r, e) -> {
//            if (e != null) {
//
//            }
//            workingJobs.remove(future);
//        }, executor);

        return greetAsFuture(name).get().getMessage();
    }
}
