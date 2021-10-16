package medicine.demo.project.converter;

import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Medicine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MedicineConverterTest {

    private MedicineConverter medicineConverter;

    @BeforeEach
    void setUp() {
        medicineConverter = new MedicineConverter();
    }

    @Test
    void toDto() {
        Medicine medicine = new Medicine(1L, "Parol", "asd");
        MedicineDto medicineDto = medicineConverter.toDto(medicine);
        assertEquals(medicine.getName(), medicineDto.getName());

    }

    @Test
    void toEntiy() {
        ArrayList<IngredientDto> ingredients = new ArrayList<>();
        ingredients.add(new IngredientDto(1L, "Parasetamol", 50.0, 1L));
        MedicineDto medicineDto = new MedicineDto(1L, "Parol", "asd", ingredients);

        Medicine medicine = medicineConverter.toEntity(medicineDto);

        assertEquals(medicineDto.getName(), medicine.getName());
    }
}