package ua.nure.plug.service;

import ua.nure.plug.model.Document;

import java.util.List;

public interface DocumentService {

    Document getById(String id);

    Document createFrom(String text);

    List<Document> getAll();

    void delete(String id);

    void deleteAll();

}
