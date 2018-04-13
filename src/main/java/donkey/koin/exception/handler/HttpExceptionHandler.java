package donkey.koin.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity handleException(HttpStatusCodeException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
}
