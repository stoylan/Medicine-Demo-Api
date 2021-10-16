package medicine.demo.project.service;

import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.entity.Ingredient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface MedicineService {
    DataResult<MedicineDto> saveMedicine(MedicineDto medicineDto);
    DataResult<MedicineDto> getById(Long id);
    DataResult<ArrayList<MedicineDto>> getAll();
    DataResult<MedicineDto> update(Long id,MedicineDto medicineDto);
    DataResult<ArrayList<Ingredient>> getIngredientsByMedicineId(Long medicineId);
    DataResult<MedicineDto> deleteById(Long id);


}
