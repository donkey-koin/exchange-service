package donkey.koin.users;

import donkey.koin.entities.user.User;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.password.HashService;
import donkey.koin.users.controller.UserRegistrationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final HashService hashService;

    @Autowired
    public UserRegistrationService(UserRepository userRepository, HashService hashService) {
        this.userRepository = userRepository;
        this.hashService = hashService;
    }

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

        userRepository.save(user);
    }
}
