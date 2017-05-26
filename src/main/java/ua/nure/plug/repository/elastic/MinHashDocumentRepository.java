package ua.nure.plug.repository.elastic;

import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ua.nure.plug.model.elastic.MinHashDocument;
import ua.nure.plug.model.elastic.ShingleDocument;
import ua.nure.plug.model.elastic.Text;


public interface MinHashDocumentRepository extends ElasticsearchRepository<MinHashDocument, String> {

    AggregatedPage<MinHashDocument> findAll();

    MinHashDocument findByDocument(String document);

}
