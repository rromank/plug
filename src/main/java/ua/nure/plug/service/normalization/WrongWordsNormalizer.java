package ua.nure.plug.service.normalization;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Qualifier(value = "wrongWordsNormalizer")
public class WrongWordsNormalizer extends Normalizer {

    private Pattern pattern = Pattern.compile("([а-яА-Яёєїі]*[oaiepukx]+[а-яА-Яёєїі]*)");

    @Override
    String normalizeText(String text) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String incorrectWord = matcher.group();
            String correctWord = correctWord(matcher.group());
            text = text.replace(incorrectWord, correctWord);
        }
        return text;
    }

    private String correctWord(String word) {
        word = word.replace('o', 'о');
        word = word.replace('a', 'а');
        word = word.replace('i', 'і');
        word = word.replace('e', 'е');
        word = word.replace('p', 'р');
        word = word.replace('u', 'и');
        word = word.replace('k', 'к');
        word = word.replace('x', 'х');
        return word;
    }

}
