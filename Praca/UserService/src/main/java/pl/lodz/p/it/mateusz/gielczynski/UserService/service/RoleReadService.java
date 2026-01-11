package pl.lodz.p.it.mateusz.gielczynski.UserService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.RoleRepository;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
@RequiredArgsConstructor
public class RoleReadService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
