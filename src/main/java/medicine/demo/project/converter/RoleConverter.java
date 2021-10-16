package medicine.demo.project.converter;

import medicine.demo.project.dto.RoleDto;
import medicine.demo.project.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {
    public RoleDto toDto(Role role){
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        if (role.getId() != null){
            roleDto.setId(role.getId());
        }
        return roleDto;
    }

    public Role toEntity(RoleDto roleDto){
        Role role = new Role();
        role.setName(roleDto.getName());
        if (roleDto.getId() != null){
            role.setId(roleDto.getId());
        }
        return role;
    }


}
