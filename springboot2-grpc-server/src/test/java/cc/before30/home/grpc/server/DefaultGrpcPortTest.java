package cc.before30.home.grpc.server;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.lognet.springboot.grpc.autoconfigure.GRpcServerProperties;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * DefaultGrpcPortTest
 *
 * @author before30
 * @since 2019-06-09
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GrpcServerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DefaultGrpcPortTest extends GrpcServerTestBase {

    @LocalRunningGrpcPort
    int runningPort;

    @Override
    protected int getPort() {
        return runningPort;
    }

    @Override
    protected void beforeGreeting() {
        Assert.assertThat(gRpcServerProperties.getPort(), CoreMatchers.nullValue(Integer.class));
        Assert.assertEquals(GRpcServerProperties.DEFAULT_GRPC_PORT, runningPort);
    }
}
