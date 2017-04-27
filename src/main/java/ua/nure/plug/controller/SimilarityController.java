package ua.nure.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Similarity;
import ua.nure.plug.service.DocumentService;
import ua.nure.plug.service.similarity.ShingleSimilarity;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/similarity")
public class SimilarityController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private ShingleSimilarity shingleSimilarity;

    @RequestMapping(value = "/shingle", method = RequestMethod.GET)
    public Similarity shingleFromText(@RequestParam("documentId1") String documentId1, @RequestParam("documentId2") String documentId2) {
        Document document1 = documentService.getById(documentId1);
        Document document2 = documentService.getById(documentId2);

        if (document1 != null && document2 != null) {
            return shingleSimilarity.similarity(document1, document2);
        }

        return null;
    }


}
