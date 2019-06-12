package cc.before30.home.grpc.server;

import cc.before30.home.grpc.server.domain.ProductItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import java.util.Arrays;
import java.util.List;

/**
 * GrpcServerApplication
 *
 * @author before30
 * @since 2019-06-09
 */

@SpringBootApplication
@EnableSolrRepositories
@Slf4j
public class GrpcServerApplication implements CommandLineRunner {

    @Autowired
    private SolrTemplate solrTemplate;

    public static void main(String[] args) {
        SpringApplication.run(GrpcServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> categories = Arrays.asList("TV", "PHONE", "WATCH", "MONITOR", "PEN", "KEYBOARD");
        for (int i = 0; i < 100; i++) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", i);
            doc.addField("title", "title_" + i + RandomStringUtils.randomAlphanumeric(5));
            doc.addField("description", "test_" + i + ":" + RandomStringUtils.randomAlphanumeric(2));
            doc.addField("price", RandomUtils.nextFloat(1_000, 10_000));
            doc.addField("category", categories.get(RandomUtils.nextInt(0, categories.size() - 1)));
            solrTemplate.saveDocument("b430", doc);
        }
        solrTemplate.commit("b430");

        Query basicQuery = new SimpleQuery("category:*TV* OR description:test_10*");
        Page<ProductItem> basicResponse = solrTemplate.query("b430", basicQuery, ProductItem.class);
        log.info("----");
        for (ProductItem item : basicResponse) {
            log.info("{}", item);
        }

        Query filterQuery = new SimpleQuery("price:[1000 TO 2000]");
        Page<ProductItem> filterResponse = solrTemplate.query("b430", filterQuery, ProductItem.class);
        log.info("----");
        for (ProductItem item : filterResponse) {
            log.info("{}", item);
        }

        Query query = new SimpleQuery("category:TV");
        Page<ProductItem> response2 = solrTemplate.query("b430", query, ProductItem.class);
        log.info("----");
        for (ProductItem item : response2) {
            log.info("{}", item);
        }


    }
}
