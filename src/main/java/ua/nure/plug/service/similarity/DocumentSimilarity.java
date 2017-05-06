package ua.nure.plug.service.similarity;

import ua.nure.plug.model.Document;
import ua.nure.plug.model.Similarity;

import java.util.Set;

public interface DocumentSimilarity {

    Similarity similarity(Document document, Set<Document> documents);

    Similarity similarity(Document document1, Document document2);

}
