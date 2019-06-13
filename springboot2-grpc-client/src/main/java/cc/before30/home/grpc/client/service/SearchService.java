package cc.before30.home.grpc.client.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * SearchService
 *
 * @author before30
 * @since 2019-06-13
 */

@Slf4j
@Component
public class SearchService {

    private final Executor executor;
    private final ScheduledExecutorService scheduledExecutor;

    public SearchService(Executor executor) {
        this.executor = executor;
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public GreeterOuterClass.SearchResponse search(String query) throws ExecutionException, InterruptedException {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 6556).usePlaintext().build();
        final GreeterGrpc.GreeterFutureStub stub = GreeterGrpc.newFutureStub(channel);
        GreeterOuterClass.SearchRequest request = GreeterOuterClass.SearchRequest.newBuilder().setQuery(query).build();


        ListenableFuture<GreeterOuterClass.SearchResponse> response = stub.search(request);

        return response.get();
    }

}
