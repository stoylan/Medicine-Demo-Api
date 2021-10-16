package medicine.demo.project.service.pgsql;

import medicine.demo.project.converter.UserConverter;
import medicine.demo.project.core.utilities.exceptions.UserNotFoundException;
import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.Response.UserRegisterResponse;
import medicine.demo.project.dto.UserDto;
import medicine.demo.project.entity.Role;
import medicine.demo.project.entity.User;
import medicine.demo.project.repository.RoleRepository;
import medicine.demo.project.repository.UserRepository;
import medicine.demo.project.service.RoleService;
import medicine.demo.project.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Service(value = "userService")
public class UserServicePgSql implements UserService, UserDetailsService {
    private final RoleService roleService;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bcryptEncoder;

    private UserConverter userConverter;

    private final RoleRepository roleRepository;

    public UserServicePgSql(RoleService roleService, UserRepository userRepository, BCryptPasswordEncoder bcryptEncoder, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.bcryptEncoder = new BCryptPasswordEncoder();
        this.roleRepository = roleRepository;
        userConverter = new UserConverter();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Yanlış kullanıcı adı yada şifre girdiniz.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    public DataResult<List<UserDto>> findAll() {
        List<UserDto> userDtoList = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(user -> userDtoList.add(userConverter.toDto(user)));

        return new SuccessDataResult<List<UserDto>>(userDtoList,"Kullanıcılar başarıyla listelendi.");
    }

    @Override
    public DataResult<UserDto> findOne(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException(username + " ismiyle aradığınız kullanıcı bulunamadı.");
        UserDto userDto = userConverter.toDto(user);
        return new SuccessDataResult<UserDto> (userDto,"Kullanıcı başarıyla bulundu.");
    }

    @Override
    public DataResult<UserRegisterResponse> save(UserDto user) {

        User nUser = userConverter.toEntity(user);
        nUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findRoleByName("USER");
        Role adminRole = roleRepository.findRoleByName("ADMIN");
        Set<Role> roleSet = new HashSet<>();

        roleSet.add(userRole);
        if (adminRole != null){
            roleSet.add(adminRole);
        }
        nUser.setRoles(roleSet);
        UserDto savedUser = userConverter.toDto(userRepository.save(nUser));
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse(savedUser.getId(),savedUser.getUsername());
        return new SuccessDataResult<UserRegisterResponse>(userRegisterResponse,"Kullanıcı başarıyla kaydedildi.");
    }


}
