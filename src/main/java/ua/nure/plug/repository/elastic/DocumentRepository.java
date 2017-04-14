package ua.nure.plug.repository.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ua.nure.plug.model.Document;

public interface DocumentRepository extends ElasticsearchRepository<Document, String> {
}
