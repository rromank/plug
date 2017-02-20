package ua.nure.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.plug.model.Document;
import ua.nure.plug.service.DocumentExtractor;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentExtractor documentExtractor;

    @PostMapping
    public Document extractDocument(@RequestParam String file) {
        return documentExtractor.extract(file);
    }

}
