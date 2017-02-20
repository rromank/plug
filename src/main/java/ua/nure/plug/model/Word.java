package ua.nure.plug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.Set;

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

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public Set<String> getForms() {
        return forms;
    }

    public void setForms(Set<String> forms) {
        this.forms = forms;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "Word{" +
                "objectId=" + objectId +
                ", noun='" + noun + '\'' +
                ", forms=" + forms +
                ", lang='" + lang + '\'' +
                '}';
    }
}
