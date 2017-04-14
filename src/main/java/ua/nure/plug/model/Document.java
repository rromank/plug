package ua.nure.plug.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@org.springframework.data.elasticsearch.annotations.Document(indexName = "plug", type = "document")
public class Document {

    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private List<Shingle> shingles;
    private List<String> words;

}
