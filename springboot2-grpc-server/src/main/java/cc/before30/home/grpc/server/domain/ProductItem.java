package cc.before30.home.grpc.server.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * ProductItem
 *
 * @author before30
 * @since 2019-06-13
 */

@SolrDocument(collection = "b430")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ProductItem {

    @Id
    @Field
    private long id;

    @Field
    private String title;

    @Field
    private String description;

    @Field
    private String category;

    @Field
    private float price;


}
