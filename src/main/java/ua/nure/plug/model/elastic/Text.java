package ua.nure.plug.model.elastic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Getter
@Setter
@Document(indexName = "plug", type = "word")
public class Text {

    @Id
    private String id = UUID.randomUUID().toString();
    private String document;
    private String text;
    private String normalized;

    public Text() {}

    public Text(String document, String text, String normalized) {
        this.document = document;
        this.text = text;
        this.normalized = normalized;
    }

}
