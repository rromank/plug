package ua.nure.plug.service.text.impl;

import org.junit.Test;
import ua.nure.plug.service.text.TextTokenizer;
import ua.nure.plug.service.text.Token;

import java.util.List;

public class TextTokenizerImplTest {

    private TextTokenizer textTokenizer = new TextTokenizerImpl();

    @Test
    public void test() {
        String text = "Тщетности я ти ми вон попыток отождествления галактических источников. Оптического излучения меньше";

        List<Token> tokens = textTokenizer.tokenize(text);


    }

}