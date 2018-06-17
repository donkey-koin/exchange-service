package donkey.koin.app;

import donkey.koin.users.registration.UserRegistrationDetails;
import donkey.koin.users.registration.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@EntityScan(basePackages = {"donkey.koin.entities"})
@EnableJpaRepositories(basePackages = {"donkey.koin.entities"})
@ComponentScan(basePackages = {"donkey.koin"})
@SpringBootApplication
public class ExchangeApplication {

    @Autowired
    private UserRegistrationService registrationService;

    public static void main(String[] args) {
        SpringApplication.run(ExchangeApplication.class, args);
    }

    @PostConstruct
    private void registerAdmin() {
        registrationService.registerUser(new UserRegistrationDetails("a", "a", "a@gmail.com"));
    }
}
