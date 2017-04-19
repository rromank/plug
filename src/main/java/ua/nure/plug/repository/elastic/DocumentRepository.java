package ua.nure.plug.repository.elastic;

import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ua.nure.plug.model.Document;

import java.util.List;

public interface DocumentRepository extends ElasticsearchRepository<Document, String> {

    AggregatedPage<Document> findAll();

}
