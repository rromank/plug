package ua.nure.plug.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentInfo {

    private String id;
    private String filename;
    private String date;
    private int shinglesCount;
    private String text;

}
