package medicine.demo.project.service.pgsql;

import medicine.demo.project.converter.IngredientConverter;
import medicine.demo.project.converter.MedicineConverter;
import medicine.demo.project.core.utilities.exceptions.MedicineNotFoundException;
import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.entity.Medicine;
import medicine.demo.project.repository.IngredientRepository;
import medicine.demo.project.repository.MedicineRepository;
import medicine.demo.project.service.MedicineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("pgsqlMedicine")
public class MedicineServicePgSql implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientConverter ingredientConverter;
    private final MedicineConverter medicineConverter;

    public MedicineServicePgSql(MedicineRepository medicineRepository, IngredientRepository ingredientRepository, IngredientConverter ingredientConverter, MedicineConverter medicineConverter) {
        this.medicineRepository = medicineRepository;
        this.ingredientRepository = ingredientRepository;
        this.ingredientConverter = new IngredientConverter();
        this.medicineConverter = new MedicineConverter();
    }


    @Override
    public DataResult<MedicineDto> saveMedicine(MedicineDto medicineDto) {

        Medicine medicine = new Medicine();
        medicine = medicineConverter.toEntity(medicineDto);
        medicineRepository.save(medicine);
        MedicineDto savedMedicineDto = medicineConverter.toDto(medicine);
        return new SuccessDataResult<MedicineDto>(savedMedicineDto, "İlaç başarıyla kaydedildi.");
    }

    @Override
    public DataResult<MedicineDto> getById(Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId).orElse(null);
        if (medicine == null) {
            throw new MedicineNotFoundException(medicineId, "Bu id'ye sahip bir ilaç bulunamadı.");
        }
        MedicineDto medicineDto = medicineConverter.toDto(medicine);
        return new SuccessDataResult<MedicineDto>(medicineDto, "İlaç id'ye göre getirildi.");

    }

    @Override
    public DataResult<ArrayList<Ingredient>> getIngredientsByMedicineId(Long medicineId) {
        ArrayList<Ingredient> ingredients = ingredientRepository.getIngredientsByMedicine_Id(medicineId);
        ArrayList<IngredientDto> ingredientDtos = new ArrayList<>();
        ingredients.stream().forEach(ingredient -> ingredientDtos.add(ingredientConverter.toDto(ingredient)));
        String medicineName = ingredients.stream().findFirst().get().getMedicine().getName();
        return new SuccessDataResult<ArrayList<IngredientDto>>(ingredientDtos, medicineName + " ilacı için etken maddeler başarıyla getirildi.");
    }

    @Override
    public DataResult<ArrayList<MedicineDto>> getAll() {
        List<Medicine> medicines = new ArrayList<>();
        medicineRepository.findAll().iterator().forEachRemaining(medicines::add);
        ArrayList<MedicineDto> medicineDtos = new ArrayList<>();

        medicines.stream().map(medicineConverter::toDto).iterator().forEachRemaining(medicineDtos::add);
        return new SuccessDataResult<ArrayList<MedicineDto>>(medicineDtos, "İlaç listesi getirildi.");
    }

    @Override
    public DataResult<MedicineDto> update(Long id, MedicineDto medicineDto) {
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        if (medicine == null) {
            throw new MedicineNotFoundException(id, "Güncellenmek istenen ilaç sistemde kayıtlı değildir.");
        }
        Medicine updatedMedicine = medicineConverter.toEntity(medicineDto);
        medicine.setName(updatedMedicine.getName());
        medicine.setBarcode(updatedMedicine.getBarcode());
        medicine.setIngredients(updatedMedicine.getIngredients());
        medicineRepository.save(medicine);
        return new SuccessDataResult<MedicineDto>(medicineConverter.toDto(medicine), "İlaç başarıyla güncellendi.");
    }

    @Override
    public DataResult<MedicineDto> deleteById(Long id) {
        MedicineDto medicineDto = getById(id).getData();
        medicineRepository.deleteById(id);
        return new SuccessDataResult<MedicineDto>(medicineDto, id + " id'ye sahip ilaç başarıyla silindi.");
    }
}
