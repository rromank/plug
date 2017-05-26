package ua.nure.plug.service;

import ua.nure.plug.model.elastic.MinHashDocument;
import ua.nure.plug.model.elastic.ShingleDocument;

import java.util.List;

public interface MinHashDocumentService {

    void create(MinHashDocument minHashDocument);

    MinHashDocument getByDocumentId(String documentId);

    List<MinHashDocument> getAll();

    List<String> documentsWithMatches(String documentId);

    List<MinHashDocument> getByDocumentIdShingles(String documentId);

    void deleteByDocumentId(String documentId);

    void deleteAll();

}
