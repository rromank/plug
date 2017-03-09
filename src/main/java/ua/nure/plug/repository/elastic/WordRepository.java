package ua.nure.plug.repository.elastic;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ua.nure.plug.model.Word;

import java.util.List;

public interface WordRepository extends ElasticsearchRepository<Word, String> {

    @Query("{\"query\":{\"match\": {\"forms\":\"?0\"}},\"size\":1}")
    Word findNoun(String wordForm);

    List<Word> findOneByForms(String forms);
}
