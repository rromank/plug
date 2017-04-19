package ua.nure.plug.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "shingle")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shingle {

    private String shingle;
    private String hash;
    private int offset;

}
