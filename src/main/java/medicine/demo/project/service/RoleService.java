package medicine.demo.project.service;

import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    DataResult<Role> findByName(String name);
    DataResult<Role> save(Role role);
}
