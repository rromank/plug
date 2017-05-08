package ua.nure.plug.repository.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ua.nure.plug.model.elastic.ShingleDocument;


public interface ShingleDocumentRepository extends ElasticsearchRepository<ShingleDocument, String> {

    ShingleDocument findByDocument(String document);

}
