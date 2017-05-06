package ua.nure.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Similarity;
import ua.nure.plug.service.DocumentService;
import ua.nure.plug.service.similarity.ShingleSimilarity;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/similarity")
public class SimilarityController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private ShingleSimilarity shingleSimilarity;

    @RequestMapping(value = "/shingle", method = RequestMethod.GET)
    public Similarity shingleSimilarity(@RequestParam("documentId1") String documentId1, @RequestParam("documentId2") String documentId2) {
        Document document1 = documentService.getById(documentId1);
        Document document2 = documentService.getById(documentId2);

        if (document1 != null && document2 != null) {
            return shingleSimilarity.similarity(document1, document2);
        }

        return null;
    }

    @RequestMapping(value = "/shingle/one", method = RequestMethod.GET)
    public Similarity shingleSimilarity(@RequestParam("documentId") String documentId) {
        Document document = documentService.getById(documentId);
        Set<Document> documents = documentService.getByDocumentShingles(document);

        if (document != null) {
            return shingleSimilarity.similarity(documents);
        }

        return null;
    }

}
