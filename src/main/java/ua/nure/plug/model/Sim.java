package ua.nure.plug.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Sim {

    private String documentId;
    private String filename;
    private double coefficient;
    private Map<String, List<Range>> ranges = new HashMap<>();

    public Sim(String documentId, double coefficient) {
        this.documentId = documentId;
        this.coefficient = coefficient;
    }

    public void addRanges(String documentId, List<Range> range) {
        if (!ranges.containsKey(documentId)) {
            ranges.put(documentId, new ArrayList<>());
        }
        ranges.get(documentId).addAll(range);
    }

}
