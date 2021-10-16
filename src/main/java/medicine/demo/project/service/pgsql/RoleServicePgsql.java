package medicine.demo.project.service.pgsql;

import medicine.demo.project.core.utilities.exceptions.RoleNotFoundException;
import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.entity.Role;
import medicine.demo.project.repository.RoleRepository;
import medicine.demo.project.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServicePgsql implements RoleService {
    private final RoleRepository roleDao;

    public RoleServicePgsql(RoleRepository roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public DataResult<Role> findByName(String name) {
        Role role = roleDao.findRoleByName(name);
        if (role ==null){
            throw new RoleNotFoundException(name + " adlı rol bulunamadı.");
        }

        return new SuccessDataResult<Role>(role,"Rol başarıyla getirildi.");
    }

    @Override
    public DataResult<Role> save(Role role) {
        Role savedRole = roleDao.save(role);
        return new SuccessDataResult<Role>(savedRole,"Kullanıcı rolü başarıyla eklendi");
    }
}
