package medicine.demo.project.service.pgsql;

import medicine.demo.project.core.utilities.exceptions.RoleNotFoundException;
import medicine.demo.project.entity.Role;
import medicine.demo.project.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class RoleServicePgsqlTest {

    @InjectMocks
    RoleServicePgsql roleService;

    @Mock
    RoleRepository roleRepository;

    Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role(1L, "USER");
    }

    @Test
    void findByName() {
        when(roleRepository.findRoleByName("USER")).thenReturn(role);

        Role foundRole = roleService.findByName(role.getName()).getData();
        assertEquals(role, foundRole);

    }

    @Test
    void save() {
        when(roleRepository.save(role)).thenReturn(role);

        Role savedRole = roleService.save(role).getData();

        assertEquals(role,savedRole);
        verify(roleRepository,times(1)).save(role);
    }

    @Test
    void given_roleName_when_findByName_then_roleNotFoundException(){
        when(roleRepository.findRoleByName(anyString())).thenReturn(null);
        RoleNotFoundException roleNotFoundException = assertThrows(RoleNotFoundException.class,() -> roleService.findByName(role.getName()));
        assertNotNull(roleNotFoundException.getMessage());
        assertEquals("USER adlı rol bulunamadı.",roleNotFoundException.getMessage());
    }


}