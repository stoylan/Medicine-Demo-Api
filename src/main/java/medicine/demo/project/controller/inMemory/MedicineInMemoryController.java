package medicine.demo.project.controller.inMemory;

import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.dto.MedicineDto;
import medicine.demo.project.service.MedicineService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("${apiVersion:}/medicines")
@RequiredArgsConstructor
public class MedicineInMemoryController {
    @Qualifier("inMemoryMedicine")
    @NonNull
    private final MedicineService medicineService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResult<MedicineDto>> add(@RequestBody MedicineDto medicineDto) {
        return ResponseEntity.ok(medicineService.saveMedicine(medicineDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataResult<ArrayList<MedicineDto>>> getAll() {
        return ResponseEntity.ok(medicineService.getAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResult<MedicineDto>> update(@RequestBody MedicineDto medicineDto, @PathVariable Long id) {
        return ResponseEntity.ok(medicineService.update(id, medicineDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataResult<MedicineDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getById(id));
    }

    @GetMapping("/{id}/ingredients")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataResult> getByMedicineId(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getIngredientsByMedicineId(id));
    }
}
