package ua.nure.plug.model.elastic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import ua.nure.plug.model.Shingle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@org.springframework.data.elasticsearch.annotations.Document(indexName = "plug", type = "text")
public class ShingleDocument {

    @Id
    private String id = UUID.randomUUID().toString();
    private String document;
    private List<Shingle> shingles = new ArrayList<>();

    public ShingleDocument() {}

    public ShingleDocument(String document, List<Shingle> shingles) {
        this.document = document;
        this.shingles = shingles;
    }

}
