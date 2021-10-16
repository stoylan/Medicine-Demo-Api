package medicine.demo.project.service;

import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.dto.IngredientDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface IngredientService {
    DataResult<IngredientDto> save(IngredientDto ingredientDto) ;
    DataResult<IngredientDto> getById(Long id);
    DataResult<ArrayList<IngredientDto>> getAll();
    DataResult<IngredientDto> update(Long id, IngredientDto ingredientDto);
    DataResult<IngredientDto> deleteById(Long id);
}
