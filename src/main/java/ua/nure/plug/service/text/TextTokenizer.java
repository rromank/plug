package ua.nure.plug.service.text;

import java.util.List;

public interface TextTokenizer {

    List<String> tokenizeWords(String text);

    List<Token> tokenize(String text);

}
