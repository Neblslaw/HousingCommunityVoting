package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request;

public record Request(
        RequestType type,
        Object body
) {
}
