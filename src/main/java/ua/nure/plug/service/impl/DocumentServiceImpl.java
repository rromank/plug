package ua.nure.plug.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang.time.FastDateFormat;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.repository.elastic.DocumentRepository;
import ua.nure.plug.service.DocumentService;
import ua.nure.plug.service.HashService;
import ua.nure.plug.service.shingle.ShingleService;
import ua.nure.plug.service.text.TextTokenizer;
import ua.nure.plug.service.text.Token;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private HashService hashService;
    @Autowired
    private TextTokenizer tokenizer;
    @Autowired
    private ShingleService shingleService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Document getById(String id) {
        return documentRepository.findOne(id);
    }

    public Document getByText(String text) {
        String id = hashService.md5(text);
        return documentRepository.findOne(id);
    }

    @Override
    public Set<Document> getByDocumentShingles(Document document) {
        Client client = elasticsearchTemplate.getClient();

        Set<String> docs = new HashSet<>();
        for (List<Shingle> shingleList : Lists.partition(document.getShingles(), 100)) {
            MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();
            for (Shingle shingle : shingleList) {
                BoolQueryBuilder bo = new BoolQueryBuilder()
                        .must(QueryBuilders.matchQuery("shingles.hash", shingle.getHash()))
                        .mustNot(QueryBuilders.matchQuery("_id", document.getId()));
                SearchRequestBuilder srb = client.prepareSearch()
                        .setQuery(bo)
                        .setSize(1);
                multiSearchRequestBuilder.add(srb);
            }

            MultiSearchResponse sr = multiSearchRequestBuilder.get();
            for (MultiSearchResponse.Item item : sr.getResponses()) {
                SearchResponse response = item.getResponse();
                response.getHits().forEach(searchHitFields -> docs.add(searchHitFields.getId()));
            }
        }
        return docs.stream()
                .map(this::getById)
                .collect(Collectors.toSet());
    }

    @Override
    public Document createFrom(String text) {
        List<Token> words = tokenizer.tokenize(text.toLowerCase());
        List<Shingle> shingles = shingleService.createShingles(words);

        Document document = new Document();
        document.setId(hashService.md5(text));
        document.setDate(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis()));
        document.setText(text);
        document.setShingles(shingles);

        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAll() {
        if (documentRepository.count() == 0) {
            return Lists.newArrayList();
        }
        return documentRepository.findAll().getContent();
    }

    @Override
    public void delete(String id) {
        documentRepository.delete(id);
    }

    @Override
    public void deleteAll() {
        documentRepository.deleteAll();
    }

}
