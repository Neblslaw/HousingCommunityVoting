package pl.lodz.p.it.mateusz.gielczynski.model;


import java.util.UUID;

public record Vote(
        String group,
        String option
) {


    public Vote(UUID groupId, String option) {
        this(groupId.toString(), option);
    }
}
