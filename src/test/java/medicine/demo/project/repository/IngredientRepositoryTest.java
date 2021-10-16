package medicine.demo.project.repository;

import medicine.demo.project.converter.IngredientConverter;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.service.IngredientService;
import medicine.demo.project.service.MedicineService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc

@SpringBootTest
@ActiveProfiles("test")
class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    private IngredientConverter ingredientConverter;

    private MedicineDto medicineDto;

    private IngredientDto ingredientDto;

    @Autowired
    @Qualifier("pgsqlMedicine")
    private MedicineService medicineService;

    @Autowired
    @Qualifier("pgsqlIngredient")
    private IngredientService ingredientService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientConverter = new IngredientConverter();
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

    @Test
    void getByMedicine_Id() {
        ArrayList<Ingredient> ingredientArrayList = ingredientRepository.getIngredientsByMedicine_Id(1L);
        ArrayList<Ingredient> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(new Ingredient());
        ArrayList<IngredientDto> ingredientDtos = new ArrayList<>();
        ingredientArrayList.iterator().forEachRemaining(ingredient -> ingredientDtos.add(ingredientConverter.toDto(ingredient)));
        assertEquals(new IngredientDto(1L, "PARASETAMOL", 10.0, 1L), ingredientDtos.get(0));
    }
}