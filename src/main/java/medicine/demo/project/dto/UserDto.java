package medicine.demo.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    @JsonIgnore
    private Set<RoleDto> roleDtos = new HashSet<>();
}