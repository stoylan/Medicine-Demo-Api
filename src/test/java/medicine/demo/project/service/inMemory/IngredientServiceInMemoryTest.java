package medicine.demo.project.service.inMemory;

import medicine.demo.project.converter.IngredientConverter;
import medicine.demo.project.converter.MedicineConverter;
import medicine.demo.project.core.utilities.exceptions.MedicineNotFoundException;
import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
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

class IngredientServiceInMemoryTest {
    IngredientServiceInMemory ingredientService;
    @Mock
    MedicineService medicineService;
    private IngredientConverter ingredientConverter;
    private MedicineConverter medicineConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientConverter = new IngredientConverter();
        medicineConverter = new MedicineConverter();
        ingredientService = new IngredientServiceInMemory(medicineService, ingredientConverter, medicineConverter);

    }

    @Test
    void save() throws MedicineNotFoundException {
        IngredientDto ingredientDto = new IngredientDto(1L, "Parasetamol", 25.0, 1L);
        ArrayList<IngredientDto> ingredientDtoArrayList = new ArrayList<>();
        ingredientDtoArrayList.add(ingredientDto);
        MedicineDto medicineDto = new MedicineDto(1L, "Parol", "ASD", ingredientDtoArrayList);
        when(medicineService.getById(anyLong())).thenReturn(new SuccessDataResult<MedicineDto>(medicineDto));
        ingredientService.save(ingredientDto);
        assertEquals(ingredientService.getById(1L).getData(), ingredientDto);

    }


    @Test
    void getById() throws MedicineNotFoundException {
        ArrayList<IngredientDto> ingredients = new ArrayList<>();
        ingredients.add(new IngredientDto(1L, "parasetamol", 500.0, 1L));
        when(ingredientService.getAll()).thenReturn(new SuccessDataResult<>(ingredients));
        ingredientService.setData(ingredients);
        IngredientDto ingredient = ingredientService.getById(1L).getData();
        assertEquals(ingredient, ingredients.get(0));
    }

    @Test
    void getAll() {
        ArrayList<IngredientDto> ingredients = new ArrayList<>();
        ingredients.add(new IngredientDto(1L, "parasetamol", 500.0, 1L));
        ingredients.add(new IngredientDto(2L, "kafein", 30.0, 1L));
        ingredients.add(new IngredientDto(3L, "asetilsalisilik asit", 10.0, 1L));
        ingredientService.setData(ingredients);
        ArrayList<IngredientDto> ingredientArrayList = ingredientService.getAll().getData();
        assertEquals(ingredientArrayList.size(), 3);
    }

    @Test
    void update() {
        ArrayList<IngredientDto> ingredients = new ArrayList<>();
        ingredients.add(new IngredientDto(1L, "parasetamol", 500.0, 1L));
        ingredientService.setData(ingredients);
        Medicine medicine = new Medicine(1L, "Parol", "asd", null);
        when(medicineService.getById(anyLong())).thenReturn(new SuccessDataResult<MedicineDto>(medicineConverter.toDto(medicine)));
        IngredientDto updatedIngredientDto = ingredientService.update(1L, ingredients.get(0)).getData();
        assertEquals(updatedIngredientDto, ingredients.get(0));
    }
}