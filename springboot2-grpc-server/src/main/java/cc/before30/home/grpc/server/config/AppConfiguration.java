package cc.before30.home.grpc.server.config;

import cc.before30.home.grpc.server.service.GreetingService;
import org.apache.solr.util.SolrCLI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * AppConfiguration
 *
 * @author before30
 * @since 2019-06-09
 */

@Configuration
public class AppConfiguration {

    @Bean
    public ExecutorService executorService() {
        return ForkJoinPool.commonPool();
    }

    @Bean
    public GreetingService greeterService(SolrTemplate solrTemplate) {
        return new GreetingService(solrTemplate);
    }

}
