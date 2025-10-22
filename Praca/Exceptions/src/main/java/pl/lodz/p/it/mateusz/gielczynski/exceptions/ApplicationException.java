package pl.lodz.p.it.mateusz.gielczynski.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends Exception {
    private final HttpStatus status;
    private final String title;

    public ApplicationException(String message, String title, HttpStatus status) {
        super(message);
        this.status = status;
        this.title = title;
    }
}