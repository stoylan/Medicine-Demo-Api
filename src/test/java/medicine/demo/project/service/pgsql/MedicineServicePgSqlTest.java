package medicine.demo.project.service.pgsql;

import medicine.demo.project.converter.IngredientConverter;
import medicine.demo.project.converter.MedicineConverter;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.entity.Medicine;
import medicine.demo.project.repository.IngredientRepository;
import medicine.demo.project.repository.MedicineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicineServicePgSqlTest {

    @InjectMocks
    MedicineServicePgSql medicineServicePgSql;

    @Mock
    MedicineRepository medicineRepository;

    @Mock
    IngredientRepository ingredientRepository;

    MedicineConverter medicineConverter;

    IngredientConverter ingredientConverter;
    Medicine medicine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        medicineConverter = new MedicineConverter();
        ingredientConverter = new IngredientConverter();
        medicine = new Medicine(1L, "PAROL", "BARCODE");
    }

    @Test
    void saveMedicine() {

        when(medicineRepository.save(any())).thenReturn(medicine);

        MedicineDto savedMedicine = medicineServicePgSql.saveMedicine(medicineConverter.toDto(medicine)).getData();

        assertEquals(medicine, medicineConverter.toEntity(savedMedicine));
        verify(medicineRepository, times(1)).save(medicine);

    }

    @Test
    void getById() {
        Optional<Medicine> medicineOptional = Optional.of(medicine);
        when(medicineRepository.findById(anyLong())).thenReturn(medicineOptional);

        MedicineDto foundMedicine = medicineServicePgSql.getById(1L).getData();
        assertEquals(medicineConverter.toEntity(foundMedicine).getId(), medicine.getId());
        verify(medicineRepository, times(1)).findById(1L);
    }

    @Test
    void getIngredientsByMedicineId() {
        IngredientDto ingredientDto = new IngredientDto(1L, "Parasetamol", 50.0, 1L);
        ArrayList<IngredientDto> ingredientDtoArrayList = new ArrayList<>();
        ingredientDtoArrayList.add(ingredientDto);
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredientDtoArrayList.iterator().forEachRemaining(ingredientDto1 -> ingredients.add(ingredientConverter.toEntity(ingredientDto1)));
        when(ingredientRepository.getIngredientsByMedicine_Id(anyLong())).thenReturn(ingredients);

        ArrayList<Ingredient> actualIngredients = medicineServicePgSql.getIngredientsByMedicineId(1L).getData();

        assertEquals(actualIngredients.size(), 1);
        verify(ingredientRepository, times(1)).getIngredientsByMedicine_Id(1L);
    }

    @Test
    void getAll() {
        ArrayList<Medicine> medicineArrayList = new ArrayList<>();
        medicineArrayList.add(medicine);
        when(medicineRepository.findAll()).thenReturn(medicineArrayList);
        ArrayList<MedicineDto> medicines = medicineServicePgSql.getAll().getData();
        assertEquals(medicines.size(), 1);
        verify(medicineRepository, times(1)).findAll();
    }

    @Test
    void update() {
        Medicine updatedData = new Medicine(1L, "Vermidon", "BARCODE");
        when(medicineRepository.findById(1L)).thenReturn(Optional.ofNullable(medicine));
        when(medicineRepository.save(any())).thenReturn(updatedData);

        MedicineDto actualMedicine = medicineServicePgSql.update(1L, medicineConverter.toDto(updatedData)).getData();
        assertEquals(actualMedicine.getName(), updatedData.getName());
        verify(medicineRepository, times(1)).save(updatedData);
        verify(medicineRepository, times(1)).findById(1L);

    }

    @Test
    void deleteById() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.ofNullable(medicine));
        MedicineDto medicineDto = medicineServicePgSql.deleteById(1L).getData();
        verify(medicineRepository, times(1)).deleteById(1L);
        verify(medicineRepository, times(1)).findById(1L);
        assertEquals(medicineConverter.toDto(medicine), medicineDto);

    }
}