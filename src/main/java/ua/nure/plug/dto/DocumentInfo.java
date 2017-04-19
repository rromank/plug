package ua.nure.plug.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentInfo {

    private String id;
    private String date;
    private int shinglesCount;
    private int wordsCount;

}
