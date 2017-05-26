package ua.nure.plug.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.elastic.MinHashDocument;
import ua.nure.plug.repository.elastic.MinHashDocumentRepository;
import ua.nure.plug.service.MinHashDocumentService;
import ua.nure.plug.service.similarity.MinHashSimilarity;

import java.util.ArrayList;
import java.util.List;

@Service
public class MinHashDocumentServiceImpl implements MinHashDocumentService  {

    @Autowired
    private MinHashDocumentRepository minHashDocumentRepository;
    @Autowired
    private MinHashSimilarity minHashSimilarity;

    @Override
    public void create(MinHashDocument minHashDocument) {
        minHashDocumentRepository.save(minHashDocument);
    }

    @Override
    public MinHashDocument getByDocumentId(String documentId) {
        return minHashDocumentRepository.findByDocument(documentId);
    }

    @Override
    public List<MinHashDocument> getAll() {
        return minHashDocumentRepository.findAll().getContent();
    }

    @Override
    public List<String> documentsWithMatches(String documentId) {
        MinHashDocument document = getByDocumentId(documentId);
        List<MinHashDocument> documents = getAll();
        List<String> documentsToCompare = new ArrayList<>();
        if (document != null && documents != null && documents.size() > 0) {
            for (MinHashDocument doc : documents) {
                if (doc.getDocument().equals(documentId)) {
                    continue;
                }
                double coefficient = minHashSimilarity.similarity(document, doc);
                if (coefficient >= 0.01) {
                    documentsToCompare.add(doc.getDocument());
                }
            }
        }
        return documentsToCompare;
    }

    @Override
    public List<MinHashDocument> getByDocumentIdShingles(String documentId) {
        return null;
    }

    @Override
    public void deleteByDocumentId(String documentId) {
        MinHashDocument minHashDocument = minHashDocumentRepository.findByDocument(documentId);
        if (minHashDocument != null) {
            minHashDocumentRepository.delete(minHashDocument);
        }
    }

    @Override
    public void deleteAll() {
        minHashDocumentRepository.deleteAll();
    }
}
