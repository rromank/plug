package ua.nure.plug.service.impl;

import org.springframework.stereotype.Component;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.service.DocumentSimilarity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShingleSimilarity implements DocumentSimilarity {

    @Override
    public double similarity(Document document1, Document document2) {
        List<Shingle> shingles = new ArrayList<>(document1.getShingles());
        shingles.retainAll(document2.getShingles());
        return 1.0 * shingles.size() / Math.min(document1.getShingles().size(), document2.getShingles().size());
    }

}
