package medicine.demo.project.service.pgsql;

import medicine.demo.project.converter.IngredientConverter;
import medicine.demo.project.core.utilities.exceptions.IngredientNotFoundException;
import medicine.demo.project.core.utilities.exceptions.MedicineNotFoundException;
import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.entity.Medicine;
import medicine.demo.project.repository.IngredientRepository;
import medicine.demo.project.repository.MedicineRepository;
import medicine.demo.project.service.IngredientService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("pgsqlIngredient")
public class IngredientServicePgSql implements IngredientService {
    private final MedicineRepository medicineRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientConverter ingredientConverter;

    public IngredientServicePgSql(MedicineRepository medicineRepository, IngredientRepository ingredientRepository, IngredientConverter ingredientConverter) {
        this.medicineRepository = medicineRepository;
        this.ingredientRepository = ingredientRepository;
        this.ingredientConverter = new IngredientConverter();
    }

    @Override
    public DataResult<IngredientDto> save(IngredientDto ingredientDto) {
        Ingredient ingredient = ingredientConverter.toEntity(ingredientDto);
        Optional<Medicine> medicineOptional = medicineRepository.findById(ingredient.getMedicine().getId());
        if (medicineOptional.isEmpty()) {
            throw new MedicineNotFoundException(ingredientDto.getMedicineId(), "Etken maddeyi kaydetmek istediğiniz ilaç bulunmamaktadır.");
        }
        Medicine medicine = medicineOptional.get();

        IngredientDto savedIngredientDto = ingredientConverter.toDto(ingredientRepository.save(ingredient));
        return new SuccessDataResult<>(savedIngredientDto, "Etken madde başarıyla " + medicine.getName() + " ilacı için kaydedildi.");

    }

    @Override
    public DataResult<IngredientDto> getById(Long id) {
        Optional<Ingredient> ingredient = (ingredientRepository.findById(id));
        if (ingredient.isEmpty()) {
            throw new IngredientNotFoundException(id, "Girilen id'ye ait bir etken madde bulunamadı");
        }
        IngredientDto ingredientDto = ingredientConverter.toDto(ingredient.get());
        return new SuccessDataResult<IngredientDto>(ingredientDto, "İd'ye göre etken madde getirildi.");
    }

    @Override
    public DataResult<IngredientDto> update(Long id, IngredientDto ingredientDto) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if (ingredient.isEmpty()) {
            throw new IngredientNotFoundException(id, "Güncellemek istediğiniz etken madde bulunmamaktadır.");
        }
        return save(ingredientDto);

    }


    @Override
    public DataResult<IngredientDto> deleteById(Long id) {
        IngredientDto ingredientDto = getById(id).getData();
        ingredientRepository.deleteById(id);
        return new SuccessDataResult<IngredientDto>(ingredientDto, id + " id'ye sahip etken madde başarıyla silindi.");
    }

    @Override
    public DataResult<ArrayList<IngredientDto>> getAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().iterator().forEachRemaining(ingredients::add);
        List<IngredientDto> ingredientDtos = new ArrayList<>();

        ingredients.stream().forEach(ingredient -> ingredientDtos.add(ingredientConverter.toDto(ingredient)));
        return new SuccessDataResult<List<IngredientDto>>(ingredientDtos, "Bütün etken maddeler listelendi.");
    }


}
