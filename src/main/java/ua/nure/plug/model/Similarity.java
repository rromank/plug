package ua.nure.plug.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Similarity {

    private double coefficient;
    private List<Range> rangeDocument1;
    private List<Range> rangeDocument2;

}
