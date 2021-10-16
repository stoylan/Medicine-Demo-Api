package medicine.demo.project.controller;

import medicine.demo.project.core.utilities.results.ErrorDataResult;
import medicine.demo.project.core.utilities.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicineNotFoundException.class)
    public ResponseEntity medicineNotFound(MedicineNotFoundException medicineNotFoundException) {
        return new ResponseEntity(new ErrorDataResult<Long>(medicineNotFoundException.getId(), medicineNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity ingredientNotFound(IngredientNotFoundException ingredientNotFoundException) {
        return new ResponseEntity(new ErrorDataResult<Long>(ingredientNotFoundException.getId(), ingredientNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity handleTokenRefreshException(TokenRefreshException ex) {
        return new ResponseEntity(
               ex.getMessage(),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = JwtTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity jwtTokenException(JwtTokenException jwtTokenException) {
        return new ResponseEntity(new ErrorDataResult<Long>(jwtTokenException.getId(),jwtTokenException.getMessage()) ,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity userNotFound(UserNotFoundException userNotFoundException) {
        return new ResponseEntity(new ErrorDataResult<Long>(userNotFoundException.getId(),userNotFoundException.getMessage()) ,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity roleNotFound(RoleNotFoundException roleNotFoundException) {
        return new ResponseEntity(new ErrorDataResult<Long>(roleNotFoundException.getId(),roleNotFoundException.getMessage()) ,HttpStatus.FORBIDDEN);
    }

}
