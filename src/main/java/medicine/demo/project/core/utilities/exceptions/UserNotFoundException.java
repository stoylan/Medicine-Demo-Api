package medicine.demo.project.core.utilities.exceptions;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
