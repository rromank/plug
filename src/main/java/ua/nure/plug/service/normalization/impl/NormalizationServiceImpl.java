package ua.nure.plug.service.normalization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.service.normalization.*;

import javax.annotation.PostConstruct;

@Service
public class NormalizationServiceImpl implements NormalizationService {

    @Autowired
    private LowercaseNormalizer lowercaseNormalizer;
    @Autowired
    private IntroductionNormalizer introductionNormalizer;
    @Autowired
    private LiteratureNormalizer literatureNormalizer;
    @Autowired
    private WrongWordsNormalizer wrongWordsNormalizer;
    @Autowired
    private NounNormalizer nounNormalizer;

    private Normalizer normalizer;

    @PostConstruct
    public void init() {
        lowercaseNormalizer.setNext(introductionNormalizer);
        introductionNormalizer.setNext(literatureNormalizer);
        literatureNormalizer.setNext(wrongWordsNormalizer);
//        wrongWordsNormalizer.setNext(nounNormalizer);
        normalizer = lowercaseNormalizer;
    }

    @Override
    public String normalize(String text) {
        return normalizer.normalize(text);
    }

}
