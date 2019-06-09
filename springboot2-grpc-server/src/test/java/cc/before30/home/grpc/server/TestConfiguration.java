package cc.before30.home.grpc.server;

import io.grpc.*;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.*;
/**
 * TestConfiguration
 *
 * @author before30
 * @since 2019-06-09
 */

@Configuration
public class TestConfiguration {

    public static final String CUSTOM_EXECUTOR_MESSAGE = "Hello from custom executor.";

    @Bean(name = "globalInterceptor")
    @GRpcGlobalInterceptor
    public ServerInterceptor globalInterceptor() {
        ServerInterceptor mock = mock(ServerInterceptor.class);
        when(mock.interceptCall(notNull(ServerCall.class),
                notNull(Metadata.class),
                notNull(ServerCallHandler.class))).thenAnswer(new Answer<ServerCall.Listener>() {

            @Override
            public ServerCall.Listener answer(InvocationOnMock invocation) throws Throwable {
                return ServerCallHandler.class.cast(invocation.getArguments()[2]).startCall(
                        ServerCall.class.cast(invocation.getArguments()[0]),
                        Metadata.class.cast(invocation.getArguments()[1]));
            }
        });

        return mock;
    }
}
