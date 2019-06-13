package cc.before30.home.grpc.server.service;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * LogInterceptor2
 *
 * @author before30
 * @since 2019-06-14
 */
@Slf4j
public class LogInterceptor2 implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        log.info("log2 : {}", call.getMethodDescriptor().getFullMethodName());
        return next.startCall(call, headers);
    }
}
