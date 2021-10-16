package medicine.demo.project.integrationTest;

import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.IngredientDto;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IngredientPgSqlControllerTest extends AbstractJwtTest<IngredientDto>{


    @Test
    void add() throws Exception {
        actualResponse = testPostControllerForAdmin("/api/withDb/1.0/ingredients",ingredientDto);

        expectedDataResult = new SuccessDataResult(ingredientDto,"Etken madde baÅ\u009FarÄ±yla PAROL ilacÄ± iÃ§in kaydedildi.");
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));

    }

    @Test
    void getAll() throws Exception {
        actualResponse = testGetControllerForUser("/api/withDb/1.0/ingredients");

        ArrayList<IngredientDto> ingredientDtoArrayList = new ArrayList<>();
        ingredientDtoArrayList.add(ingredientDto);
        expectedDataResult = new SuccessDataResult(ingredientDtoArrayList,"BÃ¼tÃ¼n etken maddeler listelendi.");
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));
    }

    @Test
    void update() throws Exception {
        ingredientDto.setName("KAFEIN");
        actualResponse = testPutControllerForAdmin("/api/withDb/1.0/ingredients/1",ingredientDto);
        expectedDataResult = new SuccessDataResult(ingredientDto,"Etken madde baÅ\u009FarÄ±yla PAROL ilacÄ± iÃ§in kaydedildi.");
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));
    }


}
