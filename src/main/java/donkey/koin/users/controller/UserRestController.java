package donkey.koin.users.controller;

import donkey.koin.users.registration.UserRegistrationDetails;
import donkey.koin.users.registration.UserRegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static donkey.koin.dictionaries.WebServicesDictionary.USERS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(USERS)
public class UserRestController {

    @Resource
    private final UserRegistrationService userRegistrationService;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public void registerUser(@RequestBody @Valid UserRegistrationDetails userRegistrationDetails) {
        userRegistrationService.registerUser(userRegistrationDetails);
    }
}
