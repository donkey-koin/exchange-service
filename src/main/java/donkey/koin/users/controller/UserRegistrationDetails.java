package donkey.koin.users.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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

    public UserRegistrationDetails(@JsonProperty("username") String username,
                                   @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }
}
