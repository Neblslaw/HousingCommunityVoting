package pl.lodz.p.it.mateusz.gielczynski.exceptions;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class InvalidParam {

    private String name;
    private String reason;
}