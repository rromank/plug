package ua.nure.plug.service.normalization;

import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ua.nure.plug.service.WordsService;
import ua.nure.plug.service.text.TextTokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier(value = "nounNormalizer")
public class NounNormalizer extends Normalizer {

    @Autowired
    private TextTokenizer tokenizer;
    @Autowired
    private WordsService wordsService;
    @Autowired
    private TransportClient client;

    @Autowired
    public NounNormalizer(TextTokenizer tokenizer, WordsService wordsService) {
        this.tokenizer = tokenizer;
        this.wordsService = wordsService;
    }

    @Override
    public String normalizeText(String text) {
        List<String> words = tokenizer.tokenizeWords(text.toLowerCase());
        return getNouns(words).stream()
                .map(wordsService::getNoun)
                .collect(Collectors.joining(" "));
    }

    private List<String> getNouns(List<String> words) {
        MultiSearchRequestBuilder requestBuilder = prepareRequestBuilder(words);

        List<String> nouns = new ArrayList<>();
        for (MultiSearchResponse.Item item : requestBuilder.get().getResponses()) {
            SearchResponse response = item.getResponse();
            if (response != null && response.getHits().hits().length >= 1) {
                nouns.add(response.getHits().getAt(0).getSource().get("noun").toString());
            }
        }
        return nouns;
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

}
