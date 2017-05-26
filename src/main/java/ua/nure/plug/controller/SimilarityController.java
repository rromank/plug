package ua.nure.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nure.plug.model.ComplexSim;
import ua.nure.plug.model.Sim;
import ua.nure.plug.model.elastic.MinHashDocument;
import ua.nure.plug.model.elastic.ShingleDocument;
import ua.nure.plug.service.MinHashDocumentService;
import ua.nure.plug.service.ShingleDocumentService;
import ua.nure.plug.service.similarity.MinHashSimilarity;
import ua.nure.plug.service.similarity.ShingleSimilarity;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/similarity")
public class SimilarityController {

    @Autowired
    private ShingleSimilarity shingleSimilarity;
    @Autowired
    private MinHashSimilarity minHashSimilarity;
    @Autowired
    private ShingleDocumentService shingleDocumentService;
    @Autowired
    private MinHashDocumentService minHashDocumentService;

    @RequestMapping(value = "/shingle/simple", method = RequestMethod.GET)
    public Sim shingleSimilaritySimple(@RequestParam("documentId1") String documentId1, @RequestParam("documentId2") String documentId2) {
        ShingleDocument document1 = shingleDocumentService.getByDocumentId(documentId1);
        ShingleDocument document2 = shingleDocumentService.getByDocumentId(documentId2);

        if (document1 != null && document2 != null) {
            return shingleSimilarity.similarity(document1, document2);
        }

        return null;
    }

    @RequestMapping(value = "/shingle/complex", method = RequestMethod.GET)
    public ComplexSim shingleSimilarityComplex(@RequestParam("document") String documentId) {
        ShingleDocument document = shingleDocumentService.getByDocumentId(documentId);
        List<ShingleDocument> documents = shingleDocumentService.getByDocumentIdShingles(documentId);

        if (document != null) {
            return shingleSimilarity.similarity(document, documents);
        }

        return null;
    }

    @RequestMapping(value = "/minhash/simple", method = RequestMethod.GET)
    public Double minhashSimilaritySimple(@RequestParam("documentId1") String documentId1, @RequestParam("documentId2") String documentId2) {
        MinHashDocument document1 = minHashDocumentService.getByDocumentId(documentId1);
        MinHashDocument document2 = minHashDocumentService.getByDocumentId(documentId2);

        if (document1 != null && document2 != null) {
            return minHashSimilarity.similarity(document1, document2);
        }

        return null;
    }

    @RequestMapping(value = "/minhash/complex", method = RequestMethod.GET)
    public ComplexSim minhashSimilarityComplex(@RequestParam("document") String documentId) {
        List<String> documentsWithMatches = minHashDocumentService.documentsWithMatches(documentId);
        ShingleDocument document = shingleDocumentService.getByDocumentId(documentId);
        List<ShingleDocument> documents = new ArrayList<>();
        for (String docId : documentsWithMatches) {
            documents.add(shingleDocumentService.getByDocumentId(docId));
        }

        if (document != null && documents.size() != 0) {
            return shingleSimilarity.similarity(document, documents);
        }

        return null;
    }

}
