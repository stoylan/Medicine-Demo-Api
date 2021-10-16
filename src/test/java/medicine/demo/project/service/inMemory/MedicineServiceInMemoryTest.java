package medicine.demo.project.service.inMemory;

import medicine.demo.project.converter.IngredientConverter;
import medicine.demo.project.converter.MedicineConverter;
import medicine.demo.project.core.utilities.exceptions.MedicineNotFoundException;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.entity.Medicine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class MedicineServiceInMemoryTest {

    MedicineServiceInMemory medicineServiceInMemory;
    private IngredientConverter ingredientConverter;
    private MedicineConverter medicineConverter;
    MedicineDto medicineDto;
    Medicine medicine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        medicineServiceInMemory = new MedicineServiceInMemory();
        ingredientConverter = new IngredientConverter();
        medicineConverter = new MedicineConverter();
        medicine = new Medicine(1L, "PAROL", "testbarcode", new ArrayList<Ingredient>());
        medicineDto = new MedicineDto(1L, "PAROL", "testbarcode", null);
    }

    @Test
    void saveMedicine() {
        medicineServiceInMemory.saveMedicine(medicineDto);

        assertEquals(medicine.getName(), medicineServiceInMemory.getById(1L).getData().getName());

    }

    @Test
    void getById() throws MedicineNotFoundException {
        ArrayList<Medicine> medicines = new ArrayList<>();
        medicines.add(medicine);
        medicineServiceInMemory.setData(medicines);

        Medicine foundMedicine = medicineConverter.toEntity(medicineServiceInMemory.getById(1L).getData());
        foundMedicine.setIngredients(null);
        assertEquals(foundMedicine.getName(), medicines.get(0).getName());

    }

    @Test
    void getAll() {
        ArrayList<Medicine> medicines = new ArrayList<>();
        medicines.add(new Medicine(1L, "PAROL", "testbarcode", null));
        medicines.add(new Medicine(2L, "VERMIDON", "testbarcode", null));
        medicines.add(new Medicine(3L, "ASPIRIN", "testbarcode", null));


        medicineServiceInMemory.setData(medicines);
        assertEquals(medicineServiceInMemory.getAll().getData().size(), 3);
    }

    @Test
    void update() throws MedicineNotFoundException {
        ArrayList<Medicine> medicines = new ArrayList<>();
        medicines.add(medicine);

        medicineServiceInMemory.setData(medicines);

        MedicineDto updatedData = medicineServiceInMemory.update(1L, medicineDto).getData();
        assertNotNull(updatedData);
    }

    @Test
    void getIngredientsByMedicineId() {
        ArrayList<IngredientDto> ingredients = new ArrayList<>();
        ingredients.add(new IngredientDto(1L, "parasetamol", 500.0, 1L));
        ingredients.add(new IngredientDto(2L, "kafein", 30.0, 1L));
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        ingredients.forEach(ingredientDto -> ingredientArrayList.add(ingredientConverter.toEntity(ingredientDto)));
        Medicine medicine = new Medicine(1L, "Parol", "ASD", ingredientArrayList);
        ArrayList<Medicine> medicines = new ArrayList<>();
        medicines.add(medicine);
        medicineServiceInMemory.setData(medicines);
        assertEquals(medicineServiceInMemory.getIngredientsByMedicineId(1L).getData().size(), 2);
    }
}