package pl.lodz.p.it.mateusz.gielczynski.exceptions;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ValidationProblemDetailsDto {
    String title;
    List<InvalidParam> invalidParams;
}