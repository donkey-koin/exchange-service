package donkey.koin.integration;

import donkey.koin.app.DonkeyKoinApplication;
import donkey.koin.users.registration.UserRegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRegistrationService repository;

    @Test
    public void test() throws Exception {
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"szymon5\",\n" +
                        "  \"password\": \"oskar12\",\n" +
                        "  \"email\": \"szymon@stypa.com\"\n" +
                        "}"))
                .andExpect(status().isCreated());

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"szymon5\",\n" +
                        "  \"password\": \"oskar12\"\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }
}