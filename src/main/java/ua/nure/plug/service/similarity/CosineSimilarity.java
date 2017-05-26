package ua.nure.plug.service.similarity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.service.text.TextTokenizer;

import java.util.HashSet;
import java.util.Set;

@Service
public class CosineSimilarity {

    @Autowired
    private TextTokenizer textTokenizer;

    public double cosine(String text1, String text2) {
        Set<String> words1 = new HashSet<>(textTokenizer.tokenizeWords(text1));
        Set<String> words2 = new HashSet<>(textTokenizer.tokenizeWords(text2));

        double divider = Math.sqrt(words1.size() * words2.size());
        words1.retainAll(words2);
        double commonUniqueWords = words1.size();

        return commonUniqueWords / divider;
    }

}
