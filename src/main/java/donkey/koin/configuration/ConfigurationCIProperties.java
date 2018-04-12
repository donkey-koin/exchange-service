package donkey.koin.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Profile("dev")
@Configuration
@PropertySource("classpath:application-ci.properties")
@ConfigurationProperties
public class ConfigurationCIProperties implements ApplicationConfigurationProperties {
}
