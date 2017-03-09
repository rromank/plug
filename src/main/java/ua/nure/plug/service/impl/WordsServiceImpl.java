package ua.nure.plug.service.impl;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Word;
import ua.nure.plug.repository.elastic.WordRepository;
import ua.nure.plug.service.WordsService;

import java.util.List;

@Log
@Service
public class WordsServiceImpl implements WordsService {

    @Autowired
    private WordRepository wordRepository;

    @Override
    public String getNoun(String word, String lang) {
        List<Word> words = wordRepository.findOneByForms(word);
        return words.size() > 0 ? words.get(0).getNoun() : word;
    }

}
