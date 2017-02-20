package ua.nure.plug.service;

import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Word;
import ua.nure.plug.repository.WordRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Service
@Log
public class WordsServiceImpl implements WordsService {

    @Autowired
    private WordRepository wordRepository;

    @Override
    public void loadWords(String filePath, String lang) {
        List<Word> words = new ArrayList<>();
        try {
            Word word = new Word(lang);
            for (String line : FileUtils.readLines(FileUtils.getFile(filePath), Charset.forName("utf-8"))) {
                if (!line.startsWith("  ")) {
                    word = new Word(lang);
                    words.add(word);
                    String noun = getWord(line);
                    word.setNoun(noun);
                    word.addForm(noun);
                } else {
                    word.addForm(getWord(line));
                }
            }
        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage());
        }
        wordRepository.save(words);
    }

    @Override
    public String getNoun(String word, String lang) {
        Word founded = wordRepository.findNoun(word, lang);
        return founded != null ? founded.getNoun() : word;
    }

    @Override
    public void deleteAll() {
        wordRepository.deleteAll();
    }

    private static String getWord(String line) {
        return line.trim().split(" ")[0];
    }

}
