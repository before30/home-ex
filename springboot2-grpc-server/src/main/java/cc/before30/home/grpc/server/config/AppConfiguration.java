package cc.before30.home.grpc.server.config;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import cc.before30.home.grpc.server.service.GreetingService;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * AppConfiguration
 *
 * @author before30
 * @since 2019-06-09
 */

@Configuration
@Slf4j
public class AppConfiguration {

    @Bean
    public Executor executor(ApplicationContext context) {
        BeanFactory beanFactory = context.getParentBeanFactory();
        return new LazyTraceExecutor(beanFactory, ForkJoinPool.commonPool());
    }

    @Bean
    public GreetingService greeterService(SolrTemplate solrTemplate) {
        return new GreetingService(solrTemplate);
    }

    @Bean
    public GrpcTracing grpcTracing(Tracing tracing) {
        return GrpcTracing.create(tracing);
    }

    @Bean
    @GRpcGlobalInterceptor
    ServerInterceptor grpcServerSleuthInterceptor(GrpcTracing grpcTracing) {
        return grpcTracing.newServerInterceptor();
    }

    @Bean
    public Reporter<Span> spanReporter() {
        return new Reporter<Span>() {
            @Override
            public void report(Span span) {
                log.info("{}", span.toString());
            }
        };
    }
}
