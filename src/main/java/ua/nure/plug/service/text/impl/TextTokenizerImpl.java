package ua.nure.plug.service.text.impl;

import org.springframework.stereotype.Component;
import ua.nure.plug.model.Range;
import ua.nure.plug.service.text.TextTokenizer;
import ua.nure.plug.service.text.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextTokenizerImpl implements TextTokenizer {

    private Pattern pattern = Pattern.compile("([а-яА-Яёєїі]+'*[а-яА-Яёєїі]+){2}");
    private Pattern allWordsPattern = Pattern.compile("[а-яА-ЯёъїЇєЄіІ\\w]+");


    @Override
    public List<String> tokenizeWords(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group().toLowerCase());
        }
        return tokens;
    }

    @Override
    public List<Token> tokenize(String text) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            tokens.add(new Token(matcher.group().toLowerCase(), Range.of(matcher.start(), matcher.end())));
        }
        return tokens;
    }

    @Override
    public List<String> tokenizeBySpace(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = allWordsPattern.matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }
    
}
