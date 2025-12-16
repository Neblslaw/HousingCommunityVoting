package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request;

import com.fasterxml.jackson.databind.JsonNode;

public record Request(
        RequestType type,
        JsonNode body
) {
}
