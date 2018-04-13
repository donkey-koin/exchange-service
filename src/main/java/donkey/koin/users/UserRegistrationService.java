package donkey.koin.users;

import donkey.koin.entities.user.User;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.users.controller.UserRegistrationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserRegistrationDetails userRegistrationDetails) {
        Optional<User> maybeUser = userRepository.findUserByUsername(userRegistrationDetails.getUsername());

        maybeUser.ifPresent(user -> {
            throw new HttpClientErrorException(CONFLICT, String.format("User %s already exists", user.getUsername()));
        });

        User user = User.builder()
                .username(userRegistrationDetails.getUsername())
                .password(userRegistrationDetails.getPassword())
                .build();

        userRepository.save(user);
    }
}
