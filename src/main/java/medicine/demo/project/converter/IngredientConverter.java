package medicine.demo.project.converter;

import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.entity.Medicine;
import org.springframework.stereotype.Component;

@Component
public class IngredientConverter {
    public Ingredient toEntity(IngredientDto dto) {
        if (dto == null)
            return null;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(dto.getId());
        ingredient.setName(dto.getName());
        ingredient.setAmount(dto.getAmount());
        if (dto.getMedicineId() != null) {
            Medicine medicine = new Medicine();
            medicine.setId(dto.getMedicineId());
            medicine.getIngredients().add(ingredient);
            ingredient.setMedicine(medicine);
        }
        return ingredient;
    }

    public IngredientDto toDto(Ingredient entity) {
        if (entity == null)
            return null;
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(entity.getId());
        ingredientDto.setAmount(entity.getAmount());
        ingredientDto.setName(entity.getName());

        if (entity.getMedicine() != null) {
            ingredientDto.setMedicineId(entity.getMedicine().getId());
        }

        return ingredientDto;
    }
}
