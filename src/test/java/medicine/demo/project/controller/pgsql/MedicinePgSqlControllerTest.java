package medicine.demo.project.controller.pgsql;

import medicine.demo.project.controller.inMemory.MedicineInMemoryController;
import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Medicine;
import medicine.demo.project.service.MedicineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MedicinePgSqlControllerTest {
    @InjectMocks
    MedicineInMemoryController medicineInMemoryController;

    @Mock
    MedicineService medicineService;

    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(medicineInMemoryController).build();

    }

    @Test
    void add() throws Exception {
        MedicineDto medicine = new MedicineDto(1L, "Aspirin", "testBarcode", null);
        when(medicineService.saveMedicine(medicine)).thenReturn(new SuccessDataResult<>(medicine));

        mockMvc.perform(post("/medicines")
                .content(objectMapper.writeValueAsString(medicine))
                .contentType("application/json"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getAll() throws Exception {
        ArrayList<Medicine> medicineArrayList = new ArrayList<>();
        medicineArrayList.add(new Medicine(1L, "Aspirin", "testBarcode", null));
        when(medicineService.getAll()).thenReturn(new SuccessDataResult<>(medicineArrayList));

        MvcResult mvcResult = mockMvc.perform(get("/medicines"))
                .andExpect(status().isOk()).andReturn();

        SuccessDataResult expectedDataResult = new SuccessDataResult(medicineArrayList);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));
    }

    @Test
    void update() throws Exception {
        Medicine medicine = new Medicine(1L, "Parol", "testBarcode", null);
        when(medicineService.update(any(), any())).thenReturn(new SuccessDataResult<>(medicine));

        MvcResult mvcResult = mockMvc.perform(put("/medicines/1").content(objectMapper.writeValueAsString(medicine))
                .contentType("application/json"))
                .andExpect(status().isOk()).andReturn();

        SuccessDataResult expectedDataResult = new SuccessDataResult(medicine);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));
    }

    @Test
    void getById() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Parol");

        when(medicineService.getById(anyLong())).thenReturn(new SuccessDataResult<Medicine>(medicine));

        MvcResult mvcResult = mockMvc.perform(get("/medicines/1"))
                .andExpect(status().isOk()).andReturn();

        SuccessDataResult expectedResult = new SuccessDataResult(medicine);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResult));
    }

    @Test
    void getByMedicineId() throws Exception {
        IngredientDto ingredientDto = new IngredientDto(1L, "Parasetamol", 50.0, 1L);
        ArrayList<IngredientDto> ingredientDtos = new ArrayList<>();
        ingredientDtos.add(ingredientDto);
        when(medicineService.getIngredientsByMedicineId(anyLong())).thenReturn(new SuccessDataResult<>(ingredientDtos));

        MvcResult mvcResult = mockMvc.perform(get("/medicines/1/ingredients"))
                .andExpect(status().isOk()).andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(new SuccessDataResult<>(ingredientDtos)));
    }
}