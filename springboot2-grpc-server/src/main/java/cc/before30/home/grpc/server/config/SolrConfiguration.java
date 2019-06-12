package cc.before30.home.grpc.server.config;

import cc.before30.home.grpc.server.GrpcServerApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import java.nio.file.Path;

/**
 * SolrConfiguration
 *
 * @author before30
 * @since 2019-06-12
 */

@Configuration
@Slf4j
public class SolrConfiguration {

    @Bean
    public EmbeddedSolrServer solrServer() {

        String solrHome = getClass().getClassLoader().getResource("solr-home").getPath();

        CoreContainer container = CoreContainer.createAndLoad(Path.of(solrHome),
                Path.of(solrHome +"/solr.xml"));
        EmbeddedSolrServer embeddedSolrServer = new EmbeddedSolrServer(container, "b430");

        return embeddedSolrServer;
    }

    @Bean
    public SolrClient solrClient(EmbeddedSolrServer solrServer) {
        return solrServer;
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }
}
