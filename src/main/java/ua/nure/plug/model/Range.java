package ua.nure.plug.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Range {

    private int start;
    private int end;

    public static Range of(int start, int end) {
        return new Range(start, end);
    }

}
