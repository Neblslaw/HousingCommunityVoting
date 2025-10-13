package pl.lodz.p.it.mateusz.gielczynski.UserService.Exceptions;


public class NotFoundException extends ApplicationException {
    public NotFoundException(String message) {
        super(message, "Not Found", org.springframework.http.HttpStatus.NOT_FOUND);
    }
}
