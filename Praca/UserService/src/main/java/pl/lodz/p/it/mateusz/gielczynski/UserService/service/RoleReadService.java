package pl.lodz.p.it.mateusz.gielczynski.UserService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleReadService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
