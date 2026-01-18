package pl.lodz.p.it.mateusz.gielczynski.exceptions;

import org.springframework.http.HttpStatus;

public class PreconditionFailedException extends ApplicationException {
    public PreconditionFailedException() {
        super("PRECONDITION_FAILED", "PRECONDITION_FAILED", HttpStatus.PRECONDITION_FAILED);
    }
}
