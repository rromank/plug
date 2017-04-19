package ua.nure.plug.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.repository.elastic.DocumentRepository;
import ua.nure.plug.service.DocumentService;
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
    private DocumentRepository documentRepository;

    @Override
    public Document createFrom(String text) {
        List<String> words = tokenizer.tokenize(text);
        List<Shingle> shingles = shingleService.createShingles(words);

        Document document = new Document();
        document.setDate(FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss.SSS").format(System.currentTimeMillis()));
        document.setWords(words);
        document.setShingles(shingles);

        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAll() {
        if (documentRepository.count() == 0) {
            return Lists.newArrayList();
        }
        return documentRepository.findAll().getContent();
    }

    @Override
    public void delete(String id) {
        documentRepository.delete(id);
    }

    @Override
    public void deleteAll() {
        documentRepository.deleteAll();
    }

}
