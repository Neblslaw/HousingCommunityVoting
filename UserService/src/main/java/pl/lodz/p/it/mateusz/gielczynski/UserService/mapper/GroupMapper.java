package pl.lodz.p.it.mateusz.gielczynski.UserService.mapper;

import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupDetailsDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;

import java.util.stream.Collectors;

public class GroupMapper {

    public static GroupDto toDto(Group group) {
        return new GroupDto(
                group.getId(),
                group.getName()
        );
    }

    public static GroupDetailsDto toDetailedDto(Group group) {
        return new GroupDetailsDto(
                group.getId(),
                group.getName(),
                group.getAccounts().stream().map(AccountMapper::toDto).collect(Collectors.toSet()),
                group.getRoles().stream().map(RoleMapper::toDto).collect(Collectors.toSet())
        );
    }
}
