package medicine.demo.project.core.utilities.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private String errorMessage;
    private Long id;

    public NotFoundException(Long id, String errorMessage) {
        super(errorMessage);
        this.id = id;
        this.errorMessage = errorMessage;
    }

    public NotFoundException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
