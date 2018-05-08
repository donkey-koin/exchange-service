package donkey.koin.users.registration;

import donkey.koin.entities.user.UserRepository;
import donkey.koin.entities.wallet.WalletRepository;
import donkey.koin.users.controller.UserRestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfiguration {

    @Bean
    UserRestController userRestController(UserRegistrationService userRegistrationService) {
        return new UserRestController(userRegistrationService);
    }

    @Bean
    UserRegistrationService userRegistrationService(UserRepository userRepository, WalletRepository walletRepository,
                                                    PasswordEncoder passwordEncoder) {
        return new UserRegistrationService(userRepository,walletRepository,passwordEncoder);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
