package donkey.koin.users.controller;

import donkey.koin.authapi.security.JwtServiceHMAC;
import donkey.koin.entities.transaction.TransactionRepository;
import donkey.koin.entities.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Resource;

@Service
@AllArgsConstructor
public class UserDetailsService {

    @Resource
    private final UserRepository userRepository;
    @Resource
    private final TransactionRepository transactionRepository;
    @Resource
    private final JwtServiceHMAC jwtServiceHMAC;

    public UserDetails getUserDetailsForJwt(String jwt) {
        return userRepository.findUserByUsername(jwtServiceHMAC.parseJwt(jwt))
                .map(user -> new UserDetails(
                        user.getUsername(),
                        user.getEmail(),
                        transactionRepository.findAllByUserId(user.getId())))
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found"));
    }
}