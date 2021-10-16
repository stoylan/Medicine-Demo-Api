package medicine.demo.project.service.inMemory;

import medicine.demo.project.converter.MedicineConverter;
import medicine.demo.project.core.utilities.exceptions.MedicineNotFoundException;
import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.core.utilities.results.SuccessDataResult;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Ingredient;
import medicine.demo.project.entity.Medicine;
import medicine.demo.project.service.MedicineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("inMemoryMedicine")
public class MedicineServiceInMemory implements MedicineService {
    protected ArrayList<Medicine> medicines;
    private Long id = 1L;
    private final MedicineConverter medicineConverter;

    public MedicineServiceInMemory() {
        this.medicineConverter = new MedicineConverter();
        medicines = new ArrayList<>();
    }

    @Override
    public DataResult<MedicineDto> saveMedicine(MedicineDto medicineDto) {
         medicineDto.setId(id++);
        Medicine medicine = medicineConverter.toEntity(medicineDto);
        medicines.add(medicine);
        return new SuccessDataResult<MedicineDto>(medicineDto, "İlaç başarıyla kaydedildi.");
    }

    @Override
    public DataResult<MedicineDto> getById(Long id) {

        Medicine medicine = medicines.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
        if (medicine == null) {
            throw new MedicineNotFoundException(id, "Böyle bir ilaç bulunmamaktadır.");
        }
        return new SuccessDataResult<MedicineDto>(medicineConverter.toDto(medicine), "İlaç id'ye göre getirildi.");
    }

    @Override
    public DataResult<ArrayList<MedicineDto>> getAll() {
        ArrayList<MedicineDto> medicineDtos = new ArrayList<>();
        medicines.iterator().forEachRemaining(medicine -> medicineDtos.add(medicineConverter.toDto(medicine)));
        return new SuccessDataResult<ArrayList<MedicineDto>>(medicineDtos, "Bütün ilaçlar getirildi.");
    }

    @Override
    public DataResult<MedicineDto> update(Long id, MedicineDto medicineDto) {
        MedicineDto oldMedicine = getById(id).getData();
        oldMedicine.setId(id);
        oldMedicine.setName(medicineDto.getName());
        oldMedicine.setBarcode(medicineDto.getBarcode());
        oldMedicine.setIngredients(medicineDto.getIngredients());
        int indexOfMedicine = medicines.indexOf(medicineConverter.toEntity(getById(id).getData()));
        medicines.set(indexOfMedicine, medicineConverter.toEntity(oldMedicine));
        return new SuccessDataResult<MedicineDto>(medicineDto, "İlaç başarıyla güncellendi.");
    }

    @Override
    public DataResult<ArrayList<Ingredient>> getIngredientsByMedicineId(Long medicineId) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        MedicineDto medicineDto = getById(medicineId).getData();
        medicineConverter.toEntity(medicineDto).getIngredients().stream().iterator().forEachRemaining(ingredients::add);
        return new SuccessDataResult<ArrayList<Ingredient>>(ingredients);
    }

    @Override
    public DataResult<MedicineDto> deleteById(Long id) {
        return null;
    }

    public void setData(ArrayList<Medicine> list) {
        medicines.addAll(list);
    }

}
