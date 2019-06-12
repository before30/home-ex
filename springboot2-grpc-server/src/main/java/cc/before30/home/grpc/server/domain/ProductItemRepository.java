package cc.before30.home.grpc.server.domain;

import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * ProductItemRepository
 *
 * @author before30
 * @since 2019-06-13
 */
public interface ProductItemRepository extends SolrCrudRepository<ProductItem, String> {
}
