package ua.nure.plug.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@org.springframework.data.elasticsearch.annotations.Document(indexName = "plug", type = "document")
public class Document {

    @Id
    @JsonIgnore
    private String id = UUID.randomUUID().toString();
    private String date;
    private List<Shingle> shingles = new ArrayList<>();
    private List<String> words;

}
