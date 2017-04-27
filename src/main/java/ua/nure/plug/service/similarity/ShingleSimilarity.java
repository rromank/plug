package ua.nure.plug.service.similarity;

import org.springframework.stereotype.Component;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Range;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.model.Similarity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShingleSimilarity implements DocumentSimilarity {

    @Override
    public Similarity similarity(Document document1, Document document2) {
//        float neumerator = Sets.intersection(Sets.newHashSet(document1.getShingles()), Sets.newHashSet(document2.getShingles())).size();
//        float denominator = Sets.union(Sets.newHashSet(document1.getShingles()), Sets.newHashSet(document2.getShingles())).size();
//        return neumerator /  Math.min(document1.getShingles().size(), document2.getShingles().size());
//        return neumerator / denominator;

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
