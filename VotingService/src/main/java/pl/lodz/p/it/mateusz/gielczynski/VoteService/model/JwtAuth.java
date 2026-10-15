package pl.lodz.p.it.mateusz.gielczynski.VoteService.model;

import java.util.List;
import java.util.UUID;

public record JwtAuth(
        UUID id,
        String name,
        List<String> roles
) {
}
