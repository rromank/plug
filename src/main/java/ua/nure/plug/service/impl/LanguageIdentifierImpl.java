package ua.nure.plug.service.impl;

import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.nure.plug.repository.elastic.WordRepository;
import ua.nure.plug.service.LanguageIdentifier;
import ua.nure.plug.service.text.TextTokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LanguageIdentifierImpl implements LanguageIdentifier {

    @Value("${text.lang.identification.count:50}")
    private Integer identificationCount;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private TextTokenizer textTokenizer;
    @Autowired
    private TransportClient client;

    @Override
    public String identifyLanguage(String text) {
        MultiSearchRequestBuilder requestBuilder = prepareRequestBuilder(textTokenizer.tokenizeWords(text));

        Map<String, Integer> langs = new HashMap<>();
        for (MultiSearchResponse.Item item : requestBuilder.get().getResponses()) {
            SearchResponse response = item.getResponse();
            if (response != null) {
                for (SearchHit hit : response.getHits()) {
                    langs.merge(hit.getSource().get("lang").toString(), 1, (a, b) -> a + b);
                }
            }
        }
        return getLang(langs);
    }

    private MultiSearchRequestBuilder prepareRequestBuilder(List<String> words) {
        MultiSearchRequestBuilder requestBuilder = client.prepareMultiSearch();
        for (String word : words) {
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
                    .setIndices("plug")
                    .setTypes("word")
                    .setQuery(QueryBuilders.matchQuery("forms", word))
                    .setSize(1);
            requestBuilder.add(searchRequestBuilder);
        }
        return requestBuilder;
    }

    private String getLang(Map<String, Integer> langs) {
        Optional<Map.Entry<String, Integer>> lang = langs.entrySet().stream()
                .max(Map.Entry.comparingByValue(Integer::compareTo));
        return lang.isPresent() ? lang.get().getKey() : "";
    }

}
