package ua.nure.plug.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Word;
import ua.nure.plug.repository.elastic.WordRepository;
import ua.nure.plug.service.LanguageIdentifier;
import ua.nure.plug.service.text.TextTokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LanguageIdentifierImpl implements LanguageIdentifier {

    @Value("${text.lang.identification.count:50}")
    private Integer identificationCount;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private TextTokenizer textTokenizer;

    @Override
    public String identifyLanguage(String text) {
        List<String> words = textTokenizer.tokenize(text);
        Map<String, Integer> langs = new HashMap<>();
        for (int i = 0; i < identificationCount && words.size() > i; i++) {
            String lang = getLang(words.get(i));
            if (lang != null) {
                if (!langs.containsKey(lang)) {
                    langs.put(lang, 1);
                } else {
                    langs.put(lang, langs.get(lang) + 1);
                }
            }
        }

        System.out.println(langs);

        String lang = "";
        int max = 0;
        for (Map.Entry<String, Integer> entry : langs.entrySet()) {
            if (entry.getValue() >= max) {
                max = entry.getValue();
                lang = entry.getKey();
            }
        }
        return lang;
    }

    private String getLang(String word) {
        List<Word> words = wordRepository.findOneByForms(word);
        return words.size() > 0 ? words.get(0).getLang() : null;
    }

}
