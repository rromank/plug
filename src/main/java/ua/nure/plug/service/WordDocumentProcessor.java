package ua.nure.plug.service;

import ua.nure.plug.model.WordDocument;

public interface WordDocumentProcessor {

    WordDocument extract(String fileName);

}
