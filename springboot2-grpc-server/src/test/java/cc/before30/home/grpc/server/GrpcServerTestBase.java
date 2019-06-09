package cc.before30.home.grpc.server;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.inprocess.InProcessChannelBuilder;
import org.apache.catalina.core.ApplicationContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lognet.springboot.grpc.GRpcServerRunner;
import org.lognet.springboot.grpc.autoconfigure.GRpcServerProperties;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * GrpcServerTestBase
 *
 * @author before30
 * @since 2019-06-09
 */


public abstract class GrpcServerTestBase {

    @Autowired(required = false)
    @Qualifier("grpcServerRunner")
    protected GRpcServerRunner grpcServerRunner;

    @Autowired(required = false)
    @Qualifier("grpcInprocessServerRunner")
    protected GRpcServerRunner grpcInprocessServerRunner;

    protected ManagedChannel channel;
    protected ManagedChannel inProcChannel;

    @LocalRunningGrpcPort
    protected int runningPort;

//    @Autowired
//    protected ApplicationContext context;

    @Autowired
    protected GRpcServerProperties gRpcServerProperties;

    @Before
    public final void setupChannels() {
        if (gRpcServerProperties.isEnabled()) {
            channel = onChannelBuild(ManagedChannelBuilder.forAddress("localhost", getPort()).usePlaintext()).build();
        }

        if (StringUtils.hasText(gRpcServerProperties.getInProcessServerName())) {
            inProcChannel = onChannelBuild(InProcessChannelBuilder.forName(gRpcServerProperties.getInProcessServerName()).usePlaintext()).build();
        }
    }

    protected int getPort(){
        return runningPort;
    }

    protected ManagedChannelBuilder<?> onChannelBuild(ManagedChannelBuilder<?> channelBuilder){
        return  channelBuilder;
    }

    protected InProcessChannelBuilder onChannelBuild(InProcessChannelBuilder channelBuilder){
        return  channelBuilder;
    }

    @After
    public final void shutdownChannels() {
        Optional.ofNullable(channel).ifPresent(ManagedChannel::shutdownNow);
        Optional.ofNullable(inProcChannel).ifPresent(ManagedChannel::shutdownNow);
    }

    @Test
    final public void simpleGreeting() throws ExecutionException, InterruptedException {
        beforeGreeting();
        String name = "John";
        final GreeterGrpc.GreeterFutureStub greeterFutureStub =
                GreeterGrpc.newFutureStub(Optional.ofNullable(channel).orElse(inProcChannel));
        final GreeterOuterClass.HelloRequest helloRequest =
                GreeterOuterClass.HelloRequest.newBuilder().setName(name).build();
        final String reply = greeterFutureStub.sayHello(helloRequest).get().getMessage();

        Assert.assertNotNull("Reply should not be null.", reply);
        Assert.assertTrue(String.format("Reply should contain name '%s'", name), reply.contains(name));

        afterGreeting();
    }

    protected void beforeGreeting() {

    }

    protected void afterGreeting() {

    }
}
