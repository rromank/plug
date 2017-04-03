package ua.nure.plug.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Document {

    private String name;
    private List<Shingle> shingles;
    private List<String> words;

}
