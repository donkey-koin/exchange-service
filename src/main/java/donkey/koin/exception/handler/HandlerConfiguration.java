package donkey.koin.exception.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    public HttpExceptionHandler httpExceptionHandler() {
        return new HttpExceptionHandler();
    }
}