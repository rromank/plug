package ua.nure.plug.repository.elastic;

import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ua.nure.plug.model.elastic.Text;

public interface TextRepository extends ElasticsearchRepository<Text, String> {

    AggregatedPage<Text> findAll();

    Text findByDocument(String document);


//    @Query("{\"query\":{\"bool\":{\"must\":[{\"match\": {\"documentId\": \"?0\"}}]}}}")
//    Text getByDocumentId(String documentId);
//    void deleteByDocumentId(String documentId);

}
