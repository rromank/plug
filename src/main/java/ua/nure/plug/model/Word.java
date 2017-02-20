package ua.nure.plug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class Word {

    private ObjectId objectId;
    private String noun;
    private Set<String> forms = new HashSet<>();
    private String lang;

    public Word(String lang) {
        this.lang = lang;
    }

    public void addForm(String wordForm) {
        forms.add(wordForm);
    }

}
