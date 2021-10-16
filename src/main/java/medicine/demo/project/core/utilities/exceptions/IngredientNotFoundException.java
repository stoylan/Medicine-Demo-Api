package medicine.demo.project.core.utilities.exceptions;

public class IngredientNotFoundException extends NotFoundException {

    public IngredientNotFoundException(Long id,String errorMessage) {
        super(id,errorMessage);
    }
}
