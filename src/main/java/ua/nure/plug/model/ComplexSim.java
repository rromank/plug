package ua.nure.plug.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ComplexSim {

    private String documentId;
    private double coefficient;
    List<Range> ranges = new ArrayList<>();
    Map<String, List<Range>> docRanges = new HashMap<>();
    List<Sim> similarities = new ArrayList<>();

    public ComplexSim(String documentId) {
        this.documentId = documentId;
    }

    public void addSim(Sim sim) {
        similarities.add(sim);
    }

    public Map<String, List<Range>> getDocRanges() {
        Map<String, List<Range>> map = new HashMap<>();
        for (Sim sim : similarities) {
            map.put(sim.getDocumentId(), sim.getRanges().get(sim.getDocumentId()));
        }
        return map;
    }

}
