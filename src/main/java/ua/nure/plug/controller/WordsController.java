package ua.nure.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.plug.service.WordsService;

@RestController
@RequestMapping("/words")
public class WordsController {

    @Autowired
    private WordsService wordsService;

    @GetMapping("/load")
    public void loadWords(@RequestParam String file, @RequestParam String lang) {
        wordsService.loadWords(file, lang);
    }

    @DeleteMapping("/all")
    public void deleteAll() {
        wordsService.deleteAll();
    }

    @GetMapping
    public String getNoun(@RequestParam String word) {
        return wordsService.getNoun(word);
    }

}
