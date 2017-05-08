package ua.nure.plug.service.normalization.impl;

import org.springframework.stereotype.Service;
import ua.nure.plug.service.normalization.*;

@Service
public class NormalizationServiceImpl implements NormalizationService {

    private Normalizer normalizer;

    public NormalizationServiceImpl() {
        normalizer = new LowercaseNormalizer();

        Normalizer introductionNormalizer = new IntroductionNormalizer();
        normalizer.setNext(introductionNormalizer);

        Normalizer literatureNormalizer = new LiteratureNormalizer();
        introductionNormalizer.setNext(literatureNormalizer);

        Normalizer wrongWordsNormalizer = new WrongWordsNormalizer();
        literatureNormalizer.setNext(wrongWordsNormalizer);
    }

    @Override
    public String normalize(String text) {
        return normalizer.normalize(text);
    }

}
