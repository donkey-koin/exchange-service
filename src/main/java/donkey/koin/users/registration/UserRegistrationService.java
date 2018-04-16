package donkey.koin.users.registration;

import donkey.koin.entities.user.User;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.password.HashService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Resource;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@Service
@AllArgsConstructor
public class UserRegistrationService {

    @Resource
    private final UserRepository userRepository;
    @Resource
    private final HashService hashService;

    public void registerUser(UserRegistrationDetails userRegistrationDetails) {
        Optional<User> maybeUser = userRepository.findUserByUsername(userRegistrationDetails.getUsername());

        maybeUser.ifPresent(user -> {
            throw new HttpClientErrorException(CONFLICT, String.format("User %s already exists", user.getUsername()));
        });

        String hashedPassword = hashService.generateHashedString(userRegistrationDetails.getPassword());

        User user = User.builder()
                .username(userRegistrationDetails.getUsername())
                .password(hashedPassword)
                .build();

        log.info("Saving user '{}' to database", user.getUsername());
        userRepository.save(user);
    }
}
