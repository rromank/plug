package ua.nure.plug.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document(indexName = "plug", type = "word")
public class Word implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();
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
