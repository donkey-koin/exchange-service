package donkey.koin.registration.controller;

import donkey.koin.registration.RegistrationResult;
import donkey.koin.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static donkey.koin.dictionaires.WebServicesDictionary.USERS_REGISTER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RegistrationRestController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationRestController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(value = USERS_REGISTER, method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity registerUser(@RequestBody @Valid RegistrationDetails registrationDetails) {
        RegistrationResult registrationResult = registrationService.registerUser(registrationDetails);
        return ResponseEntity.ok(registrationResult);
    }
}
