package ua.nure.plug.service.similarity;

import ua.nure.plug.model.ComplexSim;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.model.elastic.Document;
import ua.nure.plug.model.Sim;
import ua.nure.plug.model.Similarity;
import ua.nure.plug.model.elastic.ShingleDocument;

import java.util.List;
import java.util.Set;

public interface DocumentSimilarity {

    ComplexSim similarity(ShingleDocument document, List<ShingleDocument> documents);

    Sim similarity(ShingleDocument document, ShingleDocument document2);

}
