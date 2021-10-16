package medicine.demo.project.converter;

import medicine.demo.project.dto.UserDto;
import medicine.demo.project.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    RoleConverter roleConverter;
    public UserConverter() {
        roleConverter = new RoleConverter();
    }

    public UserDto toDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setId(user.getId());

        if (user.getRoles() != null && user.getRoles().size() > 0){
            user.getRoles().stream().iterator().forEachRemaining(role -> userDto.getRoleDtos().add(roleConverter.toDto(role)));
        }
        return userDto;

    }

    public User toEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        if (userDto.getRoleDtos() != null && userDto.getRoleDtos().size() >0){
            userDto.getRoleDtos().stream().iterator().forEachRemaining(roleDto -> user.getRoles().add(roleConverter.toEntity(roleDto)));
        }
        return user;

    }

}
