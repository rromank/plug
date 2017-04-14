package ua.nure.plug.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.nure.plug.model.Document;
import ua.nure.plug.service.DocumentExtractor;
import ua.nure.plug.service.DocumentService;

@Log4j
@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentExtractor documentExtractor;
    @Autowired
    private DocumentService documentService;

    @GetMapping
    public Iterable<Document> getAll() {
        return documentService.getAll();
    }

    @PostMapping
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        String text = documentExtractor.extractText(file);
        documentService.createFrom(text);
    }

}
