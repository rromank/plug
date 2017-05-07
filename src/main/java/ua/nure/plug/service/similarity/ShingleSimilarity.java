package ua.nure.plug.service.similarity;

import org.springframework.stereotype.Component;
import ua.nure.plug.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ShingleSimilarity implements DocumentSimilarity {


    @Override
    public ComplexSim similarity(Document document, Set<Document> documents) {
        ComplexSim complexSim = new ComplexSim(document.getId());

        List<Shingle> allShingles = new ArrayList<>();

        for (Document document2 : documents) {
            complexSim.addSim(sim(document, document2));
            allShingles.addAll(document2.getShingles());
        }

        List<Shingle> intersection = new ArrayList<>(document.getShingles());
        intersection.retainAll(allShingles);

        double coefficient = 1.0 * intersection.size() / document.getShingles().size();
        complexSim.setCoefficient(coefficient);

        return complexSim;
    }

    @Override
    public Sim sim(Document document, Document document2) {
        List<Shingle> intersection1 = new ArrayList<>(document.getShingles());
        intersection1.retainAll(document2.getShingles());

        List<Shingle> intersection2 = new ArrayList<>(document2.getShingles());
        intersection2.retainAll(document.getShingles());

        double coefficient = 1.0 * intersection1.size() / document.getShingles().size();

        Sim sim = new Sim(document.getId(), coefficient);
        sim.addRanges(document.getId(), getRanges(intersection1));
        sim.addRanges(document2.getId(), getRanges(intersection2));
        return sim;
    }

    @Override
    public Similarity similarity(Document document1, Document document2) {
        List<Shingle> intersection1 = new ArrayList<>(document1.getShingles());
        intersection1.retainAll(document2.getShingles());

        List<Shingle> intersection2 = new ArrayList<>(document2.getShingles());
        intersection2.retainAll(document1.getShingles());

        double coefficient = 1.0 * intersection1.size() / Math.min(document1.getShingles().size(), document2.getShingles().size());

        return new Similarity(coefficient, getRanges(intersection1), getRanges(intersection2));
    }

    private List<Range> getRanges(List<Shingle> shingles) {
        List<Range> ranges = new ArrayList<>();
        Range range = null;
        for (Shingle shingle : shingles) {
            Range currentRange = shingle.getRange();
            if (range == null) {
                range = currentRange;
            }
            if (range.getEnd() > currentRange.getStart()) {
                range.setEnd(currentRange.getEnd());
            } else {
                ranges.add(range);
                range = null;
            }
        }
        return ranges;
    }

}
