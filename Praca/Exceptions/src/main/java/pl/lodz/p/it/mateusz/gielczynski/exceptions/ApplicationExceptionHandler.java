package pl.lodz.p.it.mateusz.gielczynski.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@Hidden todo add swagger
@Order(2)
@ControllerAdvice
class ApplicationExceptionHandler {

    @ExceptionHandler(value = {ApplicationException.class})
    public ResponseEntity<String> handleNotFoundException(ApplicationException e, HttpServletRequest request) {

        String message = e.getMessage();
        String kebabType = message
                .replaceFirst("\\.exception$", "")
                .replaceAll("\\.", "-");

//        ProblemDetailsDto problemDetails = ProblemDetailsDto.builder()
//                .type("tag:pl.lodz.p.it.ssbd2025.ssbd01, 2025:not-found." + kebabType)
//                .title("Not Found")
//                .detail("The requested resource could not be found.")
//                .exception(e.getClass().getSimpleName())
//                .status(HttpStatus.NOT_FOUND.value())
//                .instance(request.getRequestURI())
//                .message(e.getMessage())
//                .build();


        return ResponseEntity
                .status(e.getStatus())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(e.getTitle());
    }
}
