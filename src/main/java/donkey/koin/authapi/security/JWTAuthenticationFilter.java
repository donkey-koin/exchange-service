package donkey.koin.authapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import donkey.koin.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static donkey.koin.authapi.security.SecurityConstants.HEADER_STRING;
import static donkey.koin.authapi.security.SecurityConstants.TOKEN_PREFIX;

@Slf4j
@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            User credentials = new ObjectMapper().readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException{
        String token = jwtService.generateJwt(auth);
        res.getWriter().write("{\"token\":\"" + TOKEN_PREFIX + token + "\", " +
                "\"username\":\"" + ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername() + "\"}");
        res.addHeader("Content-Type", "application/json");
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
