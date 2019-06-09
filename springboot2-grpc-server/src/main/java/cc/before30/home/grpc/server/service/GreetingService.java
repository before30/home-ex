package cc.before30.home.grpc.server.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

/**
 * GreetingService
 *
 * @author before30
 * @since 2019-06-09
 */

@Slf4j
@GRpcService(interceptors = { LogInterceptor.class })
public class GreetingService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        String message = "Hello " + request.getName();
        final GreeterOuterClass.HelloReply.Builder replyBuilder = GreeterOuterClass.HelloReply.newBuilder().setMessage(message);

        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();

        log.info("Returning " +message);
    }
}