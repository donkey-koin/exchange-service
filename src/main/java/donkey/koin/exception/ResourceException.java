package donkey.koin.exception;

import org.springframework.http.HttpStatus;

public class ResourceException extends RuntimeException {

    private HttpStatus httpStatus;

    public ResourceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
