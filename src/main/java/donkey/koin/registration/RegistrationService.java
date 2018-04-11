package donkey.koin.registration;

import donkey.koin.entities.user.User;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.exception.ResourceException;
import donkey.koin.registration.controller.RegistrationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegistrationResult registerUser(RegistrationDetails registrationDetails) {
        Optional<User> maybeUser = userRepository.findUserByUsername(registrationDetails.getUsername());

        maybeUser.ifPresent(user -> {
            throw new ResourceException(BAD_REQUEST, String.format("User %s already exists", user.getUsername()));
        });

        User user = new User();
        user.setUsername(registrationDetails.getUsername());
        user.setPassword(registrationDetails.getPassword());
        userRepository.save(user);
        return new RegistrationResult(RegistrationResultType.SUCCESS);
    }
}
