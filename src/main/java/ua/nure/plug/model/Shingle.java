package ua.nure.plug.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "shingle")
@Builder
public class Shingle {

    private String shingle;
    private String hash;
    private int offset;



}
