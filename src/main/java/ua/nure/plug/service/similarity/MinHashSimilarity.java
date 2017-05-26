package ua.nure.plug.service.similarity;

import org.springframework.stereotype.Service;
import ua.nure.plug.model.elastic.MinHashDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MinHashSimilarity {

    public double similarity(MinHashDocument signA, MinHashDocument signB) {
        int equal_count = 0;
        for (int i = 0; i < signA.getSignature().size(); i++)
            if (signA.getSignature().get(i).equals(signB.getSignature().get(i))) {
                equal_count++;
            }

        return 1.0 * equal_count / signA.getSignature().size();
    }

    public Map<String, Double> similarity(MinHashDocument document, List<MinHashDocument> documents) {
        Map<String, Double> map = new HashMap<>();
        for (MinHashDocument document2 : documents) {
            double sim = similarity(document, document2);
            if (sim > 0.02) {
                map.put(document2.getDocument(), sim);
            }
        }
        return map;
    }


}
