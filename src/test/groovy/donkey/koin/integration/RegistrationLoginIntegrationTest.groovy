package donkey.koin.integration

import donkey.koin.app.ExchangeApplication
import donkey.koin.dictionaries.WebServicesDictionary
import donkey.koin.entities.user.UserRepository
import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.util.ResourceUtils

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner)
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = ExchangeApplication)
@AutoConfigureMockMvc
@ActiveProfiles('dev')
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class RegistrationLoginIntegrationTest {

    static String CLASSPATH = 'classpath:'
    static String REGISTRATION_CREDENTIALS = CLASSPATH + 'integration/registration/registrationCredentials.json'
    static String LOGIN_CREDENTIALS = CLASSPATH + 'integration/login/loginCredentials.json'
    static String FALSE_LOGIN_CREDENTIALS = CLASSPATH + 'integration/login/falseLoginCredentials.json'

    @Autowired
    MockMvc mvc

    @Autowired
    UserRepository userRepository

    @Rule
    public ExpectedException exception = ExpectedException.none()

    @Test
    void 'should post new user, conflict when exists, successful and unsuccessful login'() {
        def registrationCredentials = FileUtils.readFileToString(ResourceUtils.getFile(REGISTRATION_CREDENTIALS))

        mvc.perform(post(WebServicesDictionary.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationCredentials))
                .andExpect(status().isCreated())

        mvc.perform(post(WebServicesDictionary.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationCredentials))
                .andExpect(status().isConflict())

        def falseLoginCredentials = FileUtils.readFileToString(ResourceUtils.getFile(FALSE_LOGIN_CREDENTIALS))

        mvc.perform(post('/login')
                .contentType(MediaType.APPLICATION_JSON)
                .content(falseLoginCredentials))
                .andExpect(status().isUnauthorized())

        String loginCredentials = FileUtils.readFileToString(ResourceUtils.getFile(LOGIN_CREDENTIALS))

        mvc.perform(post('/login')
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginCredentials))
                .andExpect(status().isOk())
                .andExpect(header().exists('Authorization'))
    }
}