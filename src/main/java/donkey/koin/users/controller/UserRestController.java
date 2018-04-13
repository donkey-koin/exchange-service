package donkey.koin.users.controller;

import donkey.koin.users.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static donkey.koin.dictionaries.WebServicesDictionary.USERS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController(USERS)
public class UserRestController {

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserRestController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public void registerUser(@RequestBody @Valid UserRegistrationDetails userRegistrationDetails) {
        userRegistrationService.registerUser(userRegistrationDetails);
    }
}
