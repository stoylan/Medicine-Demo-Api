package medicine.demo.project.service;

import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.dto.Response.UserRegisterResponse;
import medicine.demo.project.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    DataResult<UserRegisterResponse> save(UserDto user);
    DataResult<List<UserDto>> findAll();
    DataResult<UserDto> findOne(String username);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
