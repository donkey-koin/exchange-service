package donkey.koin.app;

import donkey.koin.exception.handler.HandlerConfiguration;
import donkey.koin.users.registration.UserConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"donkey.koin.entities"})
@EnableJpaRepositories(basePackages = {"donkey.koin.entities"})
@ComponentScan(basePackages = {"donkey.koin.configuration", "donkey.koin.authapi"})
@Import({UserConfiguration.class, HandlerConfiguration.class})
@SpringBootApplication
public class ExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeApplication.class, args);
    }
}