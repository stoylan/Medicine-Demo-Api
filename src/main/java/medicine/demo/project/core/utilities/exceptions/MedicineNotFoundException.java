package medicine.demo.project.core.utilities.exceptions;


public class MedicineNotFoundException extends NotFoundException {

    public MedicineNotFoundException(Long id, String errorMessage) {

        super(id, errorMessage);
    }
}
