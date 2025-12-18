package pl.lodz.p.it.mateusz.gielczynski.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
