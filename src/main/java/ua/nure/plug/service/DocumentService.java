package ua.nure.plug.service;

import ua.nure.plug.model.Document;
import ua.nure.plug.model.Shingle;

import java.util.List;
import java.util.Set;

public interface DocumentService {

    Document getById(String id);

    Document getByText(String text);

    Set<Document> getByDocumentShingles(Document document);

    Document createFrom(String text);

    List<Document> getAll();

    void delete(String id);

    void deleteAll();

}
