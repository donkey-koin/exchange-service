package donkey.koin.users.registration;

import donkey.koin.entities.user.User;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.entities.wallet.Wallet;
import donkey.koin.entities.wallet.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@Service
@AllArgsConstructor
public class UserRegistrationService {

    @Resource
    private final UserRepository userRepository;

    @Resource
    private final WalletRepository walletRepository;

    @Resource
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationDetails userRegistrationDetails) {
        Optional<User> maybeUser = userRepository.findUserByUsernameOrEmail(userRegistrationDetails.getUsername(), userRegistrationDetails.getEmail());

        maybeUser.ifPresent(checkUsernameOrEmailDuplicate(userRegistrationDetails));

        String hashedPassword = passwordEncoder.encode(userRegistrationDetails.getPassword());

        KeyPair keyPair = generateKeyPair();

        System.out.println(Objects.requireNonNull(keyPair).getPrivate().getEncoded().length);
        User user = User.builder()
                .username(userRegistrationDetails.getUsername())
                .password(hashedPassword)
                .email(userRegistrationDetails.getEmail())
                .publicKey(Objects.requireNonNull(keyPair).getPublic().getEncoded())
                .privateKey(keyPair.getPrivate().getEncoded())
                .build();

        log.info("Saving user '{}' to database", user.getUsername());
        User createdUser = userRepository.save(user);

        Wallet wallet = Wallet.builder()
                .userId(createdUser.getId())
                .amountBtc(0d)
                .amountEuro(0d)
                .build();

        walletRepository.save(wallet);
    }

    private Consumer<User> checkUsernameOrEmailDuplicate(UserRegistrationDetails userRegistrationDetails) {
        return user -> {
            if (Objects.equals(user.getUsername(), userRegistrationDetails.getUsername())) {
                throw new HttpClientErrorException(CONFLICT, String.format("User %s already exists", user.getUsername()));
            } else if (Objects.equals(user.getEmail(), userRegistrationDetails.getEmail())) {
                throw new HttpClientErrorException(CONFLICT, String.format("Email %s already exists", user.getEmail()));
            }
        };
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            return kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
