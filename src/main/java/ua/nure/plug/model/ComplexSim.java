package ua.nure.plug.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ComplexSim {

    private String documentId;
    private double coefficient;
    List<Range> ranges = new ArrayList<>();
    List<Sim> similarities = new ArrayList<>();

    public ComplexSim(String documentId) {
        this.documentId = documentId;
    }

    public void addSim(Sim sim) {
        similarities.add(sim);
    }

}
