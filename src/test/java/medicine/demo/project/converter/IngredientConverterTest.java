package medicine.demo.project.converter;

import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.entity.Medicine;
import medicine.demo.project.service.MedicineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class IngredientConverterTest {
    @Mock
    MedicineService medicineService;

    private IngredientConverter ingredientConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientConverter = new IngredientConverter();
    }

    @Test
    void toEntity() {
        IngredientDto ingredientDto = new IngredientDto(1L, "Parasetamol", 50.0, 1L);
        ArrayList<IngredientDto> ingredientDtoArrayList = new ArrayList<>();
        ingredientDtoArrayList.add(ingredientDto);
        MedicineDto medicineDto = new MedicineDto(1L, "Parol", "asd", ingredientDtoArrayList);
        when(medicineService.saveMedicine(medicineDto)).thenReturn(new SuccessDataResult<>(medicineDto));

        Medicine medicine = new Medicine(1L, "Parol", "asd", null);

        when(medicineService.getById(anyLong())).thenReturn(new SuccessDataResult<>(medicine));

        Ingredient ingredient = ingredientConverter.toEntity(ingredientDto);

        assertEquals(ingredientDto.getName(), ingredient.getName());
        assertEquals(ingredientDto.getMedicineId(), ingredient.getMedicine().getId());

    }

    @Test
    void toDto() {
        Ingredient ingredient = new Ingredient(1L, "asd", 25.0, new Medicine(1L, "asd", "asd"));
        IngredientDto ingredientDto = ingredientConverter.toDto(ingredient);
        assertEquals(ingredient.getName(), ingredientDto.getName());

    }
}