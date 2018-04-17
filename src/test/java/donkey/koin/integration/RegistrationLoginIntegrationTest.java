package donkey.koin.integration;

import donkey.koin.app.DonkeyKoinApplication;
import donkey.koin.dictionaries.WebServicesDictionary;
import donkey.koin.users.registration.UserRegistrationService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = DonkeyKoinApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class RegistrationLoginIntegrationTest {

    public static final String REGISTRATION_CREDENTIALS = "classpath:integration/registration/registrationCredentials.json";
    public static final String LOGIN_CREDENTIALS = "classpath:integration/login/loginCredentials.json";
    public static final String FALSE_LOGIN_CREDENTIALS = "classpath:integration/login/falseLoginCredentials.json";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRegistrationService repository;

    @Test
    public void test() throws Exception {
        String registrationCredentials = FileUtils.readFileToString(ResourceUtils.getFile(REGISTRATION_CREDENTIALS));

        mvc.perform(post(WebServicesDictionary.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationCredentials))
                .andExpect(status().isCreated());

        mvc.perform(post(WebServicesDictionary.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationCredentials))
                .andExpect(status().isConflict());

        String falseLoginCredentials = FileUtils.readFileToString(ResourceUtils.getFile(FALSE_LOGIN_CREDENTIALS));

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(falseLoginCredentials))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("Authorization"));

        String loginCredentials = FileUtils.readFileToString(ResourceUtils.getFile(LOGIN_CREDENTIALS));

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginCredentials))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }
}