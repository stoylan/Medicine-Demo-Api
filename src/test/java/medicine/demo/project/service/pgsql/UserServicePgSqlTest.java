package medicine.demo.project.service.pgsql;

import medicine.demo.project.converter.UserConverter;
import medicine.demo.project.core.utilities.exceptions.UserNotFoundException;
import medicine.demo.project.dto.Response.UserRegisterResponse;
import medicine.demo.project.dto.UserDto;
import medicine.demo.project.entity.Role;
import medicine.demo.project.entity.User;
import medicine.demo.project.repository.RoleRepository;
import medicine.demo.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServicePgSqlTest {

    @InjectMocks
    UserServicePgSql userService;

    @Mock
    UserRepository userRepository;

    UserConverter userConverter;

    @InjectMocks
    RoleServicePgsql roleService;

    @Mock
    RoleRepository roleRepository;

    User user;

    Role role;

    Set<Role> roleSet = new HashSet<>();

    Set<User> userSet = new HashSet<>();

    BCryptPasswordEncoder bcryptEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bcryptEncoder = new BCryptPasswordEncoder();
        userConverter = new UserConverter();
        role = new Role(1L,"USER");
        roleSet.add(role);
        user = new User(1L,"testUsername","testPassword",roleSet);
        userSet.add(user);
    }


    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(userSet);

        List<UserDto> actualResultDto = userService.findAll().getData();

        Set<User> actualResult = new HashSet<>();

        actualResultDto.stream().iterator().forEachRemaining(userDto -> actualResult.add(userConverter.toEntity(userDto)));

        assertEquals(userSet,actualResult);
    }

    @Test
    void findOne() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        UserDto foundUser = userService.findOne(user.getUsername()).getData();

        assertEquals(user,userConverter.toEntity(foundUser));
    }

    @Test
    void save() {
        when(userRepository.save(any())).thenReturn(user);
        //when(bcryptEncoder.encode(any())).thenReturn("testPassword");
        when(roleRepository.findRoleByName(any())).thenReturn(role);
        UserRegisterResponse savedUser = userService.save(userConverter.toDto(user)).getData();
        assertEquals(user.getUsername(),savedUser.getUsername());
    }

    @Test
    void given_username_when_findByUserName_then_UserNotFoundException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,() -> userService.findOne(role.getName()));
        assertNotNull(userNotFoundException.getMessage());
        assertEquals("USER ismiyle aradığınız kullanıcı bulunamadı.",userNotFoundException.getMessage());

    }
}