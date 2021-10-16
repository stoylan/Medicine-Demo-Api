package medicine.demo.project.core.utilities.exceptions;

public class RoleNotFoundException extends NotFoundException{
    public RoleNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
