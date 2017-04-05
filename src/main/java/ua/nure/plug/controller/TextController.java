package ua.nure.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nure.plug.model.Document;
import ua.nure.plug.service.DocumentService;
import ua.nure.plug.service.impl.ShingleSimilarity;

@Controller
@RequestMapping("/text")
public class TextController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private ShingleSimilarity shingleSimilarity;

    @GetMapping("/distance")
    public String distanceForm() {
        return "distanceForm";
    }

    @PostMapping("/distance")
    public String distance(@RequestParam("text1") String text1, @RequestParam("text2") String text2, Model model) {
        Document document1 = documentService.createFrom(text1);
        Document document2 = documentService.createFrom(text2);

        double similarity = shingleSimilarity.similarity(document1, document2);
        model.addAttribute("similarity", similarity);

        return "distanceForm";
    }


}
