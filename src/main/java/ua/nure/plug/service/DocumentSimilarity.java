package ua.nure.plug.service;

import ua.nure.plug.model.Document;

public interface DocumentSimilarity {

    double similarity(Document document1, Document document2);

}
