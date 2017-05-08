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
@org.springframework.data.elasticsearch.annotations.Document(indexName = "plug", type = "document")
public class Document {

    @Id
    private String id = UUID.randomUUID().toString();
    private String filename;
    private String date;

}
