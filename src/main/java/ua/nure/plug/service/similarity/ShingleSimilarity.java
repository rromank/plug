package ua.nure.plug.service.similarity;

import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Component;
import ua.nure.plug.model.ComplexSim;
import ua.nure.plug.model.Range;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.model.Sim;
import ua.nure.plug.model.elastic.ShingleDocument;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShingleSimilarity implements DocumentSimilarity {


    @Override
    public ComplexSim similarity(ShingleDocument document, List<ShingleDocument> documents) {
        ComplexSim complexSim = new ComplexSim(document.getId());

        List<Shingle> allShingles = new ArrayList<>();

        for (ShingleDocument document2 : documents) {
            complexSim.addSim(similarity(document, document2));
            allShingles.addAll(document2.getShingles());
        }

        List<Shingle> intersection = new ArrayList<>(document.getShingles());
        intersection.retainAll(allShingles);

        double coefficient = (1.0 * intersection.size() / document.getShingles().size()) * 100;
        coefficient = Precision.round(coefficient, 2);
        complexSim.setCoefficient(coefficient);

        return complexSim;
    }

    @Override
    public Sim similarity(ShingleDocument document, ShingleDocument document2) {
        List<Shingle> intersection1 = new ArrayList<>(document.getShingles());
        intersection1.retainAll(document2.getShingles());

        List<Shingle> intersection2 = new ArrayList<>(document2.getShingles());
        intersection2.retainAll(document.getShingles());

        double coefficient = (1.0 * intersection1.size() / document.getShingles().size()) * 100;
        coefficient = Precision.round(coefficient, 2);

        Sim sim = new Sim(document.getDocument(), coefficient);
        sim.addRanges(document.getDocument(), getRanges(intersection1));
        sim.addRanges(document2.getDocument(), getRanges(intersection2));
        return sim;
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
