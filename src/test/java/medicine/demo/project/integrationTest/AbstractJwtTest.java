package medicine.demo.project.integrationTest;

import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.dto.UserDto;
import medicine.demo.project.entity.Role;
import medicine.demo.project.security.jwt.TokenProvider;
import medicine.demo.project.service.IngredientService;
import medicine.demo.project.service.MedicineService;
import medicine.demo.project.service.RoleService;
import medicine.demo.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractJwtTest<T> {
    private T object;
    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    String tokenForUser;

    String tokenForAdmin;

    ObjectMapper objectMapper;

    @Autowired
            @Qualifier("pgsqlMedicine")
    MedicineService medicineService;

    @Autowired
            @Qualifier("pgsqlIngredient")
    IngredientService ingredientService;

    @Autowired
    private MockMvc mvc;

    MedicineDto medicineDto;

    IngredientDto ingredientDto;

    SuccessDataResult expectedDataResult;

    String actualResponse;

    @BeforeEach
    public void setup() {

        objectMapper = new ObjectMapper();

        UserDto user = new UserDto();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("testPassword");

        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        Role roleAdmin = new Role();
        roleAdmin.setId(2L);
        roleAdmin.setName("ADMIN");
        UserDto admin = new UserDto();
        admin.setId(2L);
        admin.setUsername("testAdmin");
        admin.setPassword("testPassword");
        if (userService.findAll().getData().size() == 0) {
        roleService.save(role);

        //register user
        userService.save(user);

        roleService.save(roleAdmin);

        userService.save(admin);
        }
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        tokenForUser = tokenProvider.generateToken(authentication);

        final Authentication authentication2 = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        admin.getUsername(),
                        admin.getPassword()
                )
        );
        tokenForAdmin = tokenProvider.generateToken(authentication2);

        medicineDto = new MedicineDto(1L,"PAROL","TestBarcode",new ArrayList<IngredientDto>());
        medicineService.saveMedicine(medicineDto);

        ingredientDto = new IngredientDto(1L,"PARASETAMOL",10.0,1L);
        ingredientService.save(ingredientDto);
    }

    @AfterEach
    public void cleanDB(@Autowired DataSource dataSource){
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("cleanDatabase.sql"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public String testPostControllerForAdmin(String url,T object) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(url)
                .content(objectMapper.writeValueAsString(object))
                .contentType("application/json"))
                .andExpect(status().isForbidden());

        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(object))
                .header("Authorization","Bearer "+ tokenForUser))
                .andExpect(status().isForbidden());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(object))
                .header("Authorization","Bearer "+ tokenForAdmin))
                .andExpect(status().isOk()).andReturn();

        return mvcResult.getResponse().getContentAsString();

    }

    public String testGetControllerForUser(String url) throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isForbidden());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization","Bearer "+ tokenForUser))
                .andExpect(status().isOk()).andReturn();

        return mvcResult.getResponse().getContentAsString();

    }

    public String testPutControllerForAdmin(String url,T object) throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(url)
                .content(objectMapper.writeValueAsString(object))
                .contentType("application/json"))
                .andExpect(status().isForbidden());

        mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(object))
                .header("Authorization","Bearer "+ tokenForUser))
                .andExpect(status().isForbidden());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(object))
                .header("Authorization","Bearer "+ tokenForAdmin))
                .andExpect(status().isOk()).andReturn();

        return mvcResult.getResponse().getContentAsString();
    }

    public String testDeleteControllerForAdmin(String url,T object) throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(object))
                .header("Authorization","Bearer "+ tokenForUser))
                .andExpect(status().isForbidden());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(object))
                .header("Authorization","Bearer "+ tokenForAdmin))
                .andExpect(status().isOk()).andReturn();

        return mvcResult.getResponse().getContentAsString();

    }
}
