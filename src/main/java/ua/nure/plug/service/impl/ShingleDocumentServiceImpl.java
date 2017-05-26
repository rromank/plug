package ua.nure.plug.service.impl;

import com.google.common.collect.Lists;
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
import ua.nure.plug.model.Shingle;
import ua.nure.plug.model.elastic.ShingleDocument;
import ua.nure.plug.repository.elastic.ShingleDocumentRepository;
import ua.nure.plug.service.ShingleDocumentService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShingleDocumentServiceImpl implements ShingleDocumentService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ShingleDocumentRepository shingleDocumentRepository;

    @Override
    public void create(ShingleDocument shingleDocument) {
        shingleDocumentRepository.save(shingleDocument);
    }

    @Override
    public ShingleDocument getByDocumentId(String documentId) {
        return shingleDocumentRepository.findByDocument(documentId);
    }

    @Override
    public List<ShingleDocument> getAll() {
        return shingleDocumentRepository.findAll().getContent();
    }

    @Override
    public List<ShingleDocument> getByDocumentIdShingles(String documentId) {
        Client client = elasticsearchTemplate.getClient();

        Set<String> docs = new HashSet<>();
        for (List<Shingle> shingleList : Lists.partition(getByDocumentId(documentId).getShingles(), 100)) {
            MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();
            for (Shingle shingle : shingleList) {
                BoolQueryBuilder bo = new BoolQueryBuilder()
                        .must(QueryBuilders.matchQuery("shingles.hash", shingle.getHash()))
                        .mustNot(QueryBuilders.matchQuery("document", documentId));
                SearchRequestBuilder srb = client.prepareSearch()
                        .setQuery(bo)
                        .setSize(1);
                multiSearchRequestBuilder.add(srb);
            }

            MultiSearchResponse sr = multiSearchRequestBuilder.get();
            for (MultiSearchResponse.Item item : sr.getResponses()) {
                SearchResponse response = item.getResponse();
                response.getHits().forEach(searchHitFields -> docs.add(searchHitFields.getSource().get("document").toString()));
            }
        }
        return docs.stream()
                .map(this::getByDocumentId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByDocumentId(String documentId) {
        ShingleDocument shingleDocument = shingleDocumentRepository.findByDocument(documentId);
        if (shingleDocument != null) {
            shingleDocumentRepository.delete(shingleDocument);
        }
    }

    @Override
    public void deleteAll() {
        shingleDocumentRepository.deleteAll();
    }
}
