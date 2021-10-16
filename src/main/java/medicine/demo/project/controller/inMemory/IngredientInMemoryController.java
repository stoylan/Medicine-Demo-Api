package medicine.demo.project.controller.inMemory;

import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.dto.IngredientDto;
import medicine.demo.project.service.IngredientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiVersion:}/ingredients")
@RequiredArgsConstructor
public class IngredientInMemoryController {
    @Qualifier("inMemoryIngredient")
    @NonNull
    private final IngredientService ingredientService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResult> add(@RequestBody IngredientDto ingredientDto) {
        return ResponseEntity.ok(ingredientService.save(ingredientDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataResult> getAll() {
        return ResponseEntity.ok(ingredientService.getAll());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResult> update(@RequestBody IngredientDto ingredientDto, @PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.update(id, ingredientDto));
    }


}
