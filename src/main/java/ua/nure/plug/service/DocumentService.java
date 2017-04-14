package ua.nure.plug.service;

import ua.nure.plug.model.Document;

public interface DocumentService {

    Document createFrom(String text);

    Iterable<Document> getAll();

}
