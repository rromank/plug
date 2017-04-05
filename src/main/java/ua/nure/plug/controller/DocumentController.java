package ua.nure.plug.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.nure.plug.service.DocumentExtractor;
import ua.nure.plug.service.LanguageIdentifier;
import ua.nure.plug.service.shingle.ShingleService;
import ua.nure.plug.service.text.TextTokenizer;

import java.util.List;

@Log4j
@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentExtractor documentExtractor;
    @Autowired
    private LanguageIdentifier languageIdentifier;
    @Autowired
    private TextTokenizer textTokenizer;
    @Autowired
    private ShingleService shingleService;

    @GetMapping("/upload")
    public String uploadForm() {
        return "uploadForm";
    }

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        String text = documentExtractor.extractText(file);
//        String lang = languageIdentifier.identifyLanguage(text);
        List<String> words = textTokenizer.tokenize(text);
//        List<String> shingles = shingleService.createShingles(words);
//        model.addAttribute("shingles", shingles);
        return "uploadForm";
    }

}
