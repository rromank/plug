package ua.nure.plug.service;

import ua.nure.plug.model.elastic.Text;

import java.util.List;

public interface TextService {

    void create(Text text);

    void deleteByDocumentId(String documentId);

    void deleteAll();

    Text getByDocumentId(String documentId);

}
