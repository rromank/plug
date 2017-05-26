package ua.nure.plug.service;

import ua.nure.plug.model.Shingle;
import ua.nure.plug.model.elastic.Document;
import ua.nure.plug.model.elastic.ShingleDocument;

import java.util.List;

public interface ShingleDocumentService {

    void create(ShingleDocument shingleDocument);

    ShingleDocument getByDocumentId(String documentId);

    List<ShingleDocument> getAll();

    List<ShingleDocument> getByDocumentIdShingles(String documentId);

    void deleteByDocumentId(String documentId);

    void deleteAll();

}
