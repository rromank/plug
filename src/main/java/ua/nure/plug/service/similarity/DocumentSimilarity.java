package ua.nure.plug.service.similarity;

import ua.nure.plug.model.ComplexSim;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Sim;
import ua.nure.plug.model.Similarity;

import java.util.Set;

public interface DocumentSimilarity {

    ComplexSim similarity(Document document, Set<Document> documents);

    Sim sim(Document document, Document document2);

    Similarity similarity(Document document1, Document document2);

}
