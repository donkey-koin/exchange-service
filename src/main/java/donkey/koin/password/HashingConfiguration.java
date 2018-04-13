package donkey.koin.password;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashingConfiguration {

    @Bean
    HashService hashService() {
        return new SimpleSHA256HashingService();
    }
}
