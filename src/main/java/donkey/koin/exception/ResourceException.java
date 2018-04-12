package donkey.koin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ResourceException extends RuntimeException {

    @Getter
    private HttpStatus httpStatus;

    public ResourceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
