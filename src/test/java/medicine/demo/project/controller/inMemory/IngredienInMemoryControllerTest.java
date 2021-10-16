package medicine.demo.project.controller.inMemory;

import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.entity.Medicine;
import medicine.demo.project.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IngredienInMemoryControllerTest {
    @Mock
    IngredientService ingredientService;

    IngredientInMemoryController ingredientInMemoryController;

    MockMvc mockMvc;

    ObjectMapper objectMapper;
    String url;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        ingredientInMemoryController = new IngredientInMemoryController(ingredientService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientInMemoryController).build();
        objectMapper = new ObjectMapper();

    }

    @Test
    void add() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        ingredient.setMedicine(medicine);

        when(ingredientService.save(any())).thenReturn(new SuccessDataResult<>(ingredient));

        mockMvc.perform(post("/ingredients").content(objectMapper.writeValueAsString(ingredient))
                .contentType("application/json").param("id", String.valueOf(1)))
                .andExpect(status().isOk());

    }

    @Test
    void getAll() throws Exception {
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        ingredientArrayList.add(new Ingredient(1L, "parasetamol", 50.0, null));
        when(ingredientService.getAll()).thenReturn(new SuccessDataResult<>(ingredientArrayList));

        MvcResult mvcResult = mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk()).andReturn();


        SuccessDataResult expectedDataResult = new SuccessDataResult(ingredientArrayList);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));
    }


    @Test
    void update() throws Exception {
        Ingredient ingredient = new Ingredient(1L, "parasetamol", 50.0, new Medicine(1L, "Aspirin", "testBarcode", null));
        when(ingredientService.update(any(), any())).thenReturn(new SuccessDataResult<>(ingredient));

        MvcResult mvcResult = mockMvc.perform(put("/ingredients/1").content(objectMapper.writeValueAsString(ingredient))
                .contentType("application/json"))
                .andExpect(status().isOk()).andReturn();

        SuccessDataResult expectedDataResult = new SuccessDataResult(ingredient);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedDataResult));
    }
}