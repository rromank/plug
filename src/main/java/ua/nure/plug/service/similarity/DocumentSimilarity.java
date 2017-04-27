package ua.nure.plug.service.similarity;

import ua.nure.plug.model.Document;
import ua.nure.plug.model.Similarity;

public interface DocumentSimilarity {

    Similarity similarity(Document document1, Document document2);

}
