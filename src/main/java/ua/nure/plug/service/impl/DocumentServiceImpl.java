package ua.nure.plug.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.service.DocumentService;
import ua.nure.plug.service.HashService;
import ua.nure.plug.service.shingle.ShingleService;
import ua.nure.plug.service.text.TextTokenizer;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private TextTokenizer tokenizer;
    @Autowired
    private ShingleService shingleService;
    @Autowired
    private HashService hashService;

    @Override
    public Document createFrom(String text) {
        List<String> words = tokenizer.tokenize(text);
        List<Shingle> shingles = shingleService.createShingles(words);

        return Document.builder()
                .name(hashService.md5(text))
                .words(words)
                .shingles(shingles)
                .build();
    }

}
