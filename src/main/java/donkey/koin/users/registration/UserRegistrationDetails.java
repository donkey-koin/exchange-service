package donkey.koin.users.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class UserRegistrationDetails {

    @NotNull
    @NotBlank
    private final String username;

    @NotNull
    @NotBlank
    @Size(min = 6)
    private final String password;

    @Email
    @NotNull
    @NotBlank
    private final String email;

    public UserRegistrationDetails(@JsonProperty("username") String username,
                                   @JsonProperty("password") String password,
                                   @JsonProperty("email") String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
