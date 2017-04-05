package ua.nure.plug.service.text.impl;

import org.springframework.stereotype.Component;
import ua.nure.plug.service.text.TextTokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextTokenizerImpl implements TextTokenizer {

    private Pattern pattern = Pattern.compile("([а-яА-Яёєїі]+'*[а-яА-Яёєїі]+){2}");

    @Override
    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group().toLowerCase());
        }
        return tokens;
    }

}
