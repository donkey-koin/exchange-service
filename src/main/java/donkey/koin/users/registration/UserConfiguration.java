package donkey.koin.users.registration;

import donkey.koin.entities.user.UserRepository;
import donkey.koin.password.HashService;
import donkey.koin.password.SimpleSHA256HashingService;
import donkey.koin.users.controller.UserRestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

    @Bean
    UserRestController userRestController(UserRegistrationService userRegistrationService) {
        return new UserRestController(userRegistrationService);
    }

    @Bean(name = "sha256Service")
    HashService hashService() {
        return new SimpleSHA256HashingService();
    }

    @Bean
    UserRegistrationService userRegistrationService(UserRepository userRepository, HashService sha256Service) {
        return new UserRegistrationService(userRepository, sha256Service);
    }
}
