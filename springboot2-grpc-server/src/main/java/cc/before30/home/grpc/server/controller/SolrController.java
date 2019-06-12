package cc.before30.home.grpc.server.controller;

import cc.before30.home.grpc.server.domain.ProductItem;
import cc.before30.home.grpc.server.domain.ProductItemRepository;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * SolrController
 *
 * @author before30
 * @since 2019-06-13
 */
@RestController
@RequestMapping("/solr")
public class SolrController {

    private final ProductItemRepository repository;
    private final SolrTemplate solrTemplate;

    public SolrController(ProductItemRepository repository, SolrTemplate solrTemplate) {
        this.repository = repository;
        this.solrTemplate = solrTemplate;
    }

    @GetMapping("/repo")
    public Iterable<ProductItem> findByRepo() {
        return repository.findAll();
    }

    @GetMapping("/template")
    public Iterable<ProductItem> findByTemplate() {
        Query query = new SimpleQuery("*:*");
        return solrTemplate.queryForPage("b430", query, ProductItem.class);
    }

    @GetMapping("/repo/{value}")
    public Optional<ProductItem> findById(@PathVariable String value) {
        return repository.findById(value);
    }

    @GetMapping("/template/{value}")
    public Iterable<ProductItem> findByTemplateValue(@PathVariable String value) {
        Query query = new SimpleQuery(value);
        return solrTemplate.queryForPage("b430", query, ProductItem.class);
    }
}
