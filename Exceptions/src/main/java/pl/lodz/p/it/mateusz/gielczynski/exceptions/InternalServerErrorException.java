package pl.lodz.p.it.mateusz.gielczynski.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ApplicationException {
    public InternalServerErrorException(String message) {
        super(message, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
