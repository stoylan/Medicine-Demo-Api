package medicine.demo.project.converter;

import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Medicine;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MedicineConverter {
    private final IngredientConverter ingredientConverter;

    public MedicineConverter() {
        ingredientConverter = new IngredientConverter();
    }

    public MedicineDto toDto(Medicine entity) {
        if (entity == null)
            return null;
        MedicineDto medicineDto = new MedicineDto();
        medicineDto.setId(entity.getId());
        medicineDto.setBarcode(entity.getBarcode());
        medicineDto.setName(entity.getName());
        if (entity.getIngredients() != null && entity.getIngredients().size() > 0) {
            entity.getIngredients()
                    .forEach(ingredient -> medicineDto.getIngredients().add(ingredientConverter.toDto(ingredient)));
        }
        return medicineDto;
    }

    public Medicine toEntity(MedicineDto dto) {
        if (dto == null)
            return null;
        Medicine medicine = new Medicine();
        if (dto.getId() != null) {
            medicine.setId(dto.getId());
        }
        medicine.setName(dto.getName());
        medicine.setBarcode(dto.getBarcode());
        ArrayList<IngredientDto> ingredientDtos = new ArrayList<>();
        ingredientDtos.add(new IngredientDto());

        if (dto.getIngredients() != null && dto.getIngredients().size() > 0) {
            dto.getIngredients().forEach(ingredient -> medicine.getIngredients().add(ingredientConverter.toEntity(ingredient)));
        }
        return medicine;
    }
}
