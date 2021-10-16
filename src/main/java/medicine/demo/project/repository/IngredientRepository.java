package medicine.demo.project.repository;

import medicine.demo.project.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    ArrayList<Ingredient> getIngredientsByMedicine_Id(Long medicineId);
}
