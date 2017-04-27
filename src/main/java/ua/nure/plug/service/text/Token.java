package ua.nure.plug.service.text;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.nure.plug.model.Range;

@Getter
@Setter
@AllArgsConstructor
public class Token {

    private String token;
    private Range range;

}
