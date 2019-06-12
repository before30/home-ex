package cc.before30.home.grpc.server.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import cc.before30.home.grpc.server.domain.ProductItem;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * GreetingService
 *
 * @author before30
 * @since 2019-06-09
 */

@Slf4j
@GRpcService(interceptors = { LogInterceptor.class })
public class GreetingService extends GreeterGrpc.GreeterImplBase {

    private final SolrTemplate solrTemplate;

    public GreetingService(SolrTemplate solrTemplate) {
        this.solrTemplate = solrTemplate;
    }

    @Override
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        String message = "Hello " + request.getName();
        final GreeterOuterClass.HelloReply.Builder replyBuilder = GreeterOuterClass.HelloReply.newBuilder().setMessage(message);

        try {
            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(1, 3));
        } catch (InterruptedException e) {
        }
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();

        log.info("Returning " +message);
    }

    @Override
    public void search(GreeterOuterClass.SearchRequest request, StreamObserver<GreeterOuterClass.SearchResponse> responseObserver) {
        String q = request.getQuery();
        Query query = new SimpleQuery(q);
        Page<ProductItem> searchResult = solrTemplate.query("b430", query, ProductItem.class);

        for (ProductItem item : searchResult) {
            log.info("item : {}", item);
        }

        long totalCount = searchResult.getTotalElements();
//        List<Long> items = searchResult.get().map(item -> item.getId()).collect(Collectors.toList());

        GreeterOuterClass.SearchResponse response = GreeterOuterClass.SearchResponse.newBuilder().setQuery(q).setTotalCount(totalCount).build();
        try {
            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(1, 3));
        } catch (InterruptedException e) {
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
