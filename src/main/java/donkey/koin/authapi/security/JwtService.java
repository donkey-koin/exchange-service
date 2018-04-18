package donkey.koin.authapi.security;

import org.springframework.security.core.Authentication;

public interface JwtService {
    String parseJwt(String token);

    String generateJwt(Authentication auth);
}
