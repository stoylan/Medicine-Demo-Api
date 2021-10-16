package medicine.demo.project.service.pgsql;

import medicine.demo.project.converter.IngredientConverter;
import medicine.demo.project.converter.MedicineConverter;
import medicine.demo.project.core.utilities.exceptions.IngredientNotFoundException;
import medicine.demo.project.core.utilities.exceptions.MedicineNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IngredientServicePgSqlTest {

    @InjectMocks
    IngredientServicePgSql ingredientServicePgSql;

    @Mock
    IngredientRepository ingredientRepository;

    @Mock
    MedicineRepository medicineRepository;

    IngredientConverter ingredientConverter;

    MedicineConverter medicineConverter;

    IngredientDto ingredientDto;

    Ingredient ingredient;

    MedicineDto medicineDto;

    ArrayList<IngredientDto> ingredientArrayList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientConverter = new IngredientConverter();
        medicineConverter = new MedicineConverter();
        ingredientDto = new IngredientDto(1L, "Parasetamol", 50.0, 1L);
        ingredient = ingredientConverter.toEntity(ingredientDto);
        ingredientArrayList = new ArrayList<>();
        ingredientArrayList.add(ingredientDto);
        medicineDto = new MedicineDto(1L, "Parol", "Barcode", ingredientArrayList);
    }

    @Test
    void save() {
        IngredientDto command = new IngredientDto();
        command.setId(3L);
        command.setMedicineId(2L);

        Optional<Medicine> medicineOptional = Optional.of(new Medicine());

        Medicine savedMedicine = new Medicine();
        savedMedicine.addIngredient(new Ingredient());
        savedMedicine.getIngredients().iterator().next().setId(3L);

        when(medicineRepository.findById(anyLong())).thenReturn(medicineOptional);
        when(ingredientRepository.save(any())).thenReturn(savedMedicine.getIngredients().stream().findFirst().get());

        IngredientDto savedCommand = ingredientServicePgSql.save(command).getData();

        assertEquals(Long.valueOf(3L), savedCommand.getId());
        verify(medicineRepository, times(1)).findById(anyLong());
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));

    }

    @Test
    void getById() {
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.of(ingredient));
        IngredientDto actualResult = ingredientServicePgSql.getById(1L).getData();

        assertNotNull(actualResult);
        verify(ingredientRepository, times(1)).findById(1L);
    }

    @Test
    void update() {
        IngredientDto command = new IngredientDto();
        command.setId(3L);
        command.setMedicineId(2L);

        Optional<Medicine> medicineOptional = Optional.of(new Medicine());

        Medicine medicine = new Medicine();
        medicine.addIngredient(new Ingredient());
        medicine.getIngredients().iterator().next().setId(3L);

        when(medicineRepository.findById(anyLong())).thenReturn(medicineOptional);
        when(ingredientRepository.save(any())).thenReturn(medicine.getIngredients().stream().findFirst().get());

        IngredientDto savedCommand = ingredientServicePgSql.save(command).getData();

        assertEquals(Long.valueOf(3L), savedCommand.getId());
        verify(medicineRepository, times(1)).findById(anyLong());
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }


    @Test
    void deleteById() {
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.of(ingredient));

        IngredientDto ingredientDto = ingredientServicePgSql.deleteById(1L).getData();

        assertNotNull(ingredientDto);
        verify(ingredientRepository, times(1)).findById(1L);


    }

    @Test
    void getAll() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredientArrayList.iterator().forEachRemaining(ingredientDto1 -> ingredients.add(ingredientConverter.toEntity(ingredientDto1)));
        when(ingredientRepository.findAll()).thenReturn(ingredients);

        ArrayList<IngredientDto> actualIngredient = ingredientServicePgSql.getAll().getData();

        assertEquals(actualIngredient.size(), 1);
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    void given_medicineIdNotInDb_when_saveIngredient_then_throwMedicineNotFoundException(){
        when(medicineRepository.findById(anyLong())).thenReturn(Optional.empty());
        MedicineNotFoundException medicineNotFoundException = assertThrows(MedicineNotFoundException.class,() -> ingredientServicePgSql.save(ingredientDto));
        assertNotNull(medicineNotFoundException.getMessage());
        assertEquals("Etken maddeyi kaydetmek istediğiniz ilaç bulunmamaktadır.",medicineNotFoundException.getMessage());
    }

    @Test
    void given_ingredientIdIsNotInDb_when_getById_then_throwIngredientNotFoundException(){
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.empty());
        IngredientNotFoundException ingredientNotFoundException = assertThrows(IngredientNotFoundException.class, () -> ingredientServicePgSql.getById(ingredientDto.getId()));
        assertNotNull(ingredientNotFoundException.getMessage());
        assertEquals("Girilen id'ye ait bir etken madde bulunamadı",ingredientNotFoundException.getMessage());
    }
}