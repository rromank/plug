package ua.nure.plug.service.normalization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntroductionNormalizer extends Normalizer {

    private static final String[] INTRODUCTION_TITLES = {
            "вступ",
            "вступление",
            "ввод",
            "введение"};

    @Override
    String normalizeText(String text) {
        Optional<String> title = getTitles().stream()
                .filter(text::contains)
                .findFirst();
        if (title.isPresent()) {
            int index = text.indexOf(title.get());
            return text.substring(index + title.get().length()).trim();
        }
        return text;
    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        for (String title : INTRODUCTION_TITLES) {
            titles.add(title + "\r");
            titles.add(title + "\n");
            titles.add(title + "\r\n");
        }
        return titles;
    }

}
