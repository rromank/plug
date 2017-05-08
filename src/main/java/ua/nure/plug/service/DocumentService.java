package ua.nure.plug.service;

import ua.nure.plug.model.elastic.Document;

import java.util.List;
import java.util.Set;

public interface DocumentService {

    Document getById(String id);

    Document getByText(String text);

    Document createFrom(String filename, String text);

    List<Document> getAll();

    void delete(String id);

    void deleteAll();

}
