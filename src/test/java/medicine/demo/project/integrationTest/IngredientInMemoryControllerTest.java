package medicine.demo.project.integrationTest;

import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.service.MedicineService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientInMemoryControllerTest extends AbstractJwtTest<IngredientDto>{

    @Autowired
    @Qualifier("inMemoryMedicine")
    MedicineService medicineService;

    @BeforeEach
    void setUp() {
        medicineService.saveMedicine(medicineDto);
    }

    @Test
    @Order(1)
    void add() throws Exception {
        actualResponse = testPostControllerForAdmin("/api/1.0/ingredients",ingredientDto);

        expectedDataResult = new SuccessDataResult(ingredientDto,"Etken madde baÅ\u009FarÄ±yla PAROL ilacÄ± iÃ§in kaydedildi.");
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));

    }

    @Test
    void getAll() throws Exception {
        actualResponse = testGetControllerForUser("/api/1.0/ingredients");

        ArrayList<IngredientDto> ingredientDtoArrayList = new ArrayList<>();
        ingredientDtoArrayList.add(ingredientDto);
        expectedDataResult = new SuccessDataResult(ingredientDtoArrayList,"BÃ¼tÃ¼n etken maddeler getirildi.");
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));

    }

    @Test
    void update() throws Exception {
        ingredientDto.setName("KAFEIN");
        actualResponse = testPutControllerForAdmin("/api/1.0/ingredients/1",ingredientDto);

        expectedDataResult = new SuccessDataResult(ingredientDto,"Etken madde baÅ\u009FarÄ±yla gÃ¼ncellendi.");
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));
    }
}