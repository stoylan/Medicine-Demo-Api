package medicine.demo.project.integrationTest;

import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.MedicineDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MedicineInMemoryControllerTest extends AbstractJwtTest<MedicineDto> {


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

    }

    @Test
    @Order(1)
    void add() throws Exception {
        actualResponse = testPostControllerForAdmin("/api/1.0/medicines",medicineDto);
        expectedDataResult = new SuccessDataResult(medicineDto,"Ä°laÃ§ baÅ\u009FarÄ±yla kaydedildi.");
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));


    }

    @Test
    void getAll() throws Exception {
        actualResponse = testGetControllerForUser("/api/1.0/medicines");

        ArrayList<MedicineDto> medicineDtoArrayList = new ArrayList<>();
        medicineDtoArrayList.add(medicineDto);
        expectedDataResult = new SuccessDataResult(medicineDtoArrayList,"BÃ¼tÃ¼n ilaÃ§lar getirildi.");
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));
    }

    @Test
    void update() throws Exception {
        medicineDto.setName("PAROL");
        actualResponse = testPutControllerForAdmin("/api/1.0/medicines/1",medicineDto);
        expectedDataResult = new SuccessDataResult(medicineDto,"Ä°laÃ§ baÅ\u009FarÄ±yla gÃ¼ncellendi.");
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));
    }
}