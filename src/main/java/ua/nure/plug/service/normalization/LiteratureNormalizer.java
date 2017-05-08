package ua.nure.plug.service.normalization;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier(value = "literatureNormalizer")
public class LiteratureNormalizer extends Normalizer {

    private static final String[] LITERATURE_TITLES = {
            "библиографический список",
            "литература",
            "список литературы",
            "список использованной литературы",
            "использованная литература",
            "список использованных источников",
            "использованные источники",
            "література",
            "список літератури",
            "список використаної літератури",
            "використана література",
            "використані джерела",
            "список використаних джерел"
    };

    @Override
    String normalizeText(String text) {
        Optional<String> title = getTitles().stream()
                .filter(text::contains)
                .findFirst();
        if (title.isPresent()) {
            int index = text.indexOf(title.get());
            return text.substring(0, index).trim();
        }
        return text;
    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        for (String title : LITERATURE_TITLES) {
            titles.add(title + "\r");
            titles.add(title + "\n");
            titles.add(title + "\r\n");
        }
        return titles;
    }
}
