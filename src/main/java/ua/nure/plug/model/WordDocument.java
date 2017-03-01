package ua.nure.plug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class WordDocument {

    private String objectId;
    private String fullText;
    private List<String> words = new ArrayList<>();
    private List<String> nouns = new ArrayList<>();

    public void addWord(String word) {
        words.add(word);
    }

    public void addNoun(String noun) {
        nouns.add(noun);
    }

}
