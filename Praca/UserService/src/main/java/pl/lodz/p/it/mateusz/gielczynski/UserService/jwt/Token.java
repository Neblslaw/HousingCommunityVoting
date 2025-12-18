package pl.lodz.p.it.mateusz.gielczynski.UserService.jwt;

public record Token(
        String auth,
        String refresh
) {
}
