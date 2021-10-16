package medicine.demo.project.core.utilities.exceptions;

public class JwtTokenException extends NotFoundException{

    public JwtTokenException(String errorMessage) {
        super(errorMessage);
    }
}
