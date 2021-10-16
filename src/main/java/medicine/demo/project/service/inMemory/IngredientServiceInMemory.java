package medicine.demo.project.service.inMemory;

import medicine.demo.project.converter.IngredientConverter;
import medicine.demo.project.converter.MedicineConverter;
import medicine.demo.project.core.utilities.exceptions.IngredientNotFoundException;
import medicine.demo.project.core.utilities.exceptions.MedicineNotFoundException;
import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.entity.Medicine;
import medicine.demo.project.service.IngredientService;
import medicine.demo.project.service.MedicineService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("inMemoryIngredient")
public class IngredientServiceInMemory implements IngredientService {

    private final MedicineService medicineService;
    protected ArrayList<Ingredient> ingredientArrayList;
    private Long id = 1L;
    private final IngredientConverter ingredientConverter;
    private final MedicineConverter medicineConverter;

    public IngredientServiceInMemory(@Qualifier("inMemoryMedicine") MedicineService medicineService, IngredientConverter ingredientConverter, MedicineConverter medicineConverter) {
        this.medicineService = medicineService;
        this.ingredientConverter = ingredientConverter;
        this.medicineConverter = medicineConverter;
        ingredientArrayList = new ArrayList<>();
    }

    @Override
    public DataResult<IngredientDto> save(IngredientDto ingredientDto) {
        Medicine medicine = medicineConverter.toEntity(medicineService.getById(ingredientDto.getMedicineId()).getData());
        if (medicine == null) {
            throw new MedicineNotFoundException(ingredientDto.getMedicineId(), "Böyle bir ilaç bulunmamaktadır.");
        }
        Ingredient ingredient = ingredientConverter.toEntity(ingredientDto);
        if (ingredient == null)
            throw new IngredientNotFoundException(ingredientDto.getMedicineId(), "Girilen etken madde boştur.");
        ingredient.setMedicine(medicine);
        ingredient.setId(id++);
        medicine.getIngredients().add(ingredient);
        ingredientArrayList.add(ingredient);
        medicineService.saveMedicine(medicineConverter.toDto(medicine));
        return new SuccessDataResult<IngredientDto>(ingredientConverter.toDto(ingredient), "Etken madde başarıyla " + medicine.getName() + " ilacı için kaydedildi.");

    }

    @Override
    public DataResult<IngredientDto> getById(Long id) {
        IngredientDto ingredient = getAll().getData().stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);

        if (ingredient == null) {
            throw new IngredientNotFoundException(id, "Böyle bir ilaç bulunmamaktadır.");
        }
        return new SuccessDataResult<IngredientDto>(ingredient, "İlaç id'ye göre getirildi.");

    }

    @Override
    public DataResult<ArrayList<IngredientDto>> getAll() {
        ArrayList<IngredientDto> ingredientDtoArrayList = new ArrayList<>();
        ingredientArrayList.forEach(ingredient -> ingredientDtoArrayList.add(ingredientConverter.toDto(ingredient)));
        return new SuccessDataResult<ArrayList<IngredientDto>>(ingredientDtoArrayList, "Bütün etken maddeler getirildi.");
    }

    @Override
    public DataResult<IngredientDto> update(Long id, IngredientDto ingredientDto) {
        IngredientDto oldIngredient = getById(id).getData();
        ArrayList<IngredientDto> ingredientDtoArrayList = getAll().getData();
        int indexOfIngredient = ingredientDtoArrayList.indexOf(oldIngredient);
        if (oldIngredient == null)
            throw new IngredientNotFoundException(id, "Böyle bir etken madde bulunmamıştır.");
        oldIngredient.setName(ingredientDto.getName());
        oldIngredient.setAmount(ingredientDto.getAmount());
        oldIngredient.setMedicineId(medicineService.getById(ingredientDto.getMedicineId()).getData().getId());

        ingredientArrayList.set(indexOfIngredient, ingredientConverter.toEntity(oldIngredient));

        return new SuccessDataResult<IngredientDto>(ingredientDto, "Etken madde başarıyla güncellendi.");

    }

    @Override
    public DataResult<IngredientDto> deleteById(Long id) {
        return null;
    }

    public void setData(ArrayList<IngredientDto> list) {
        list.iterator().forEachRemaining(ingredientDto -> ingredientArrayList.add(ingredientConverter.toEntity(ingredientDto)));
    }

}
