package ua.nure.plug.service;

import ua.nure.plug.model.WordDocument;

public interface DocumentExtractor {

    WordDocument extract(String fileName);

}
