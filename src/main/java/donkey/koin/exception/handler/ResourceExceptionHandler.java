package donkey.koin.exception.handler;

import donkey.koin.exception.ResourceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity handleException(ResourceException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
