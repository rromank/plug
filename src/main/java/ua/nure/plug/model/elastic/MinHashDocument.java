package ua.nure.plug.model.elastic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@org.springframework.data.elasticsearch.annotations.Document(indexName = "plug", type = "minhash")
public class MinHashDocument {

    @Id
    private String id = UUID.randomUUID().toString();
    private String document;
    private List<Integer> signature = new ArrayList<>();

    public MinHashDocument() {}

    public MinHashDocument(String document, List<Integer> signature) {
        this.document = document;
        this.signature = signature;
    }

}
