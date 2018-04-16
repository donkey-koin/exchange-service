package donkey.koin.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@ControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity handleException(HttpStatusCodeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
}
