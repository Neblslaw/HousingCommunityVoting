package pl.lodz.p.it.mateusz.gielczynski.UserService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mateusz.gielczynski.UserService.consts.i18n;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.RoleRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleWriteService {

    private final RoleRepository roleRepository;


    public Role createRole(String name) throws BadRequestException {
        Role role = new Role(name);
        try {
            return roleRepository.saveAndFlush(role);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException(i18n.ROLE_ALREADY_EXISTS);
        }

    }

    public void deleteRole(UUID id) {
        roleRepository.deleteRoleById(id);
    }
}
