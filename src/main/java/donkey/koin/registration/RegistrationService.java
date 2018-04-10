package donkey.koin.registration;

import donkey.koin.entities.user.User;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.registration.controller.RegistrationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegistrationResult registerUser(RegistrationDetails registrationDetails) {
        Optional<User> userByUsername = userRepository.findUserByUsername(registrationDetails.getUsername());

        return userByUsername
                .map(user -> new RegistrationResult(RegistrationResultType.FAILURE))
                .orElseGet(() -> {
                    User user = new User();
                    user.setUsername(registrationDetails.getUsername());
                    user.setPassword(registrationDetails.getPassword());
                    userRepository.save(user);
                    return new RegistrationResult(RegistrationResultType.SUCCESS);
                });
    }
}
