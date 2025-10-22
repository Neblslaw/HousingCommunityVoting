package pl.lodz.p.it.mateusz.gielczynski.UserService.mapper;

import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountDetailsDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;

import java.util.stream.Collectors;

public class AccountMapper {

    public static AccountDto toDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getLogin(),
                account.getEmail(),
                account.getFirstName(),
                account.getLastName()
        );
    }

    public static AccountDetailsDto toDetailedDto(Account account) {
        return new AccountDetailsDto(
                account.getId(),
                account.getLogin(),
                account.getEmail(),
                account.getFirstName(),
                account.getLastName(),
                account.isArchived(),
                account.isVerified(),
                account.getGroups().stream().flatMap(group -> group.getRoles().stream()).collect(Collectors.toSet()).stream().map(RoleMapper::toDto).collect(Collectors.toSet()),
                account.getGroups().stream().map(GroupMapper::toDto).collect(Collectors.toSet())
        );
    }
}
