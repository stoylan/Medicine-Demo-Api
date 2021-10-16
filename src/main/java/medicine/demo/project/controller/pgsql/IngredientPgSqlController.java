package medicine.demo.project.controller.pgsql;

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
@RequestMapping("${apiDb:}/ingredients")
@RequiredArgsConstructor
public class IngredientPgSqlController {
    @Qualifier("pgsqlIngredient") @NonNull private final IngredientService ingredientService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResult> add(@RequestBody IngredientDto ingredientDto)  {
        return ResponseEntity.ok(ingredientService.save(ingredientDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataResult> getAll(){
        return ResponseEntity.ok(ingredientService.getAll());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResult> update(@RequestBody IngredientDto ingredientDto, @PathVariable Long id){
        return ResponseEntity.ok(ingredientService.update(id,ingredientDto));
    }


}
