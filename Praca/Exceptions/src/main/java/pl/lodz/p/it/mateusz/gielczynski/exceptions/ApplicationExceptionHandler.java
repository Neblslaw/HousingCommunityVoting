package pl.lodz.p.it.mateusz.gielczynski.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Order(2)
@ControllerAdvice
class ApplicationExceptionHandler {

    @ExceptionHandler(value = {ApplicationException.class})
    public ResponseEntity<String> handleException(ApplicationException e, HttpServletRequest request) {
        return ResponseEntity
                .status(e.getStatus())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationProblemDetailsDto> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {

        List<InvalidParam> invalidParams = exception
                .getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new InvalidParam(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ValidationProblemDetailsDto problemDetails = ValidationProblemDetailsDto.builder()
                .title("Validation failed")
                .invalidParams(invalidParams)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetails);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ValidationProblemDetailsDto> handleValidationException(ConstraintViolationException exception, HttpServletRequest request) {
//        List<InvalidParam> invalidParams = exception.getConstraintViolations().stream()
//                .map(fieldError -> new InvalidParam(fieldError.getPropertyPath().toString(), fieldError.getMessage()))
//                .toList();
//
//        ValidationProblemDetailsDto problemDetails = ValidationProblemDetailsDto.builder()
//                .title("Validation failed")
//                .invalidParams(invalidParams)
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
//                .body(problemDetails);
//    }
}
