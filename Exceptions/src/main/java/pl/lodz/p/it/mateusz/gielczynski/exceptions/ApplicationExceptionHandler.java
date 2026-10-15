package pl.lodz.p.it.mateusz.gielczynski.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Order(2)
@ControllerAdvice
class ApplicationExceptionHandler {

    private static final Log log = LogFactory.getLog(ApplicationExceptionHandler.class);

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

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleUnauthorized(AuthorizationDeniedException exception, HttpServletRequest request) {
        log.error("Unauthorized");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body("Unauthorized");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknownException(Exception exception, HttpServletRequest request) {
        log.error("Unknown exception");
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body("Internal server error");
    }
}
