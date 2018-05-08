package donkey.koin.integration

import donkey.koin.app.ExchangeApplication
import donkey.koin.entities.wallet.WalletRepository
import donkey.koin.users.registration.UserRegistrationDetails
import donkey.koin.users.registration.UserRegistrationService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ExchangeApplication)
@AutoConfigureMockMvc
@ActiveProfiles('dev')
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class WalletIntegrationTest {

    @Autowired
    MockMvc mvc

    @Autowired
    WalletRepository walletRepository

    @Autowired
    UserRegistrationService userRegistrationService

    @Test
    void 'should create a wallet'() {
        given: 'user exists in db'
        def username = 'szymo080'
        def userDetails = new UserRegistrationDetails(
                username,
                'szymon123',
                'szymo@mail.com'
        )

        when: 'user is saved'
        userRegistrationService.registerUser(userDetails)

        then: 'user has wallet'
        def wallet = walletRepository.findWalletByUsername(username).get()
        assert wallet.amountBtc == 0d
        assert wallet.amountEuro == 0d

        when: 'user deposits money'
        def depositJson = "{\"username\": \"${username}\", \"moneyToDeposit\": 1500, \"moneyToWithdrawn\": 0}"
        mvc.perform(post('/wallet/deposit')
                .contentType(MediaType.APPLICATION_JSON)
                .content(depositJson))
                .andExpect(status().isOk())

        then: 'user has more money'
        wallet = walletRepository.findWalletByUsername(username).get()
        assert wallet.amountBtc == 0d
        assert wallet.amountEuro == 1500d


        when: 'user withdraws money'
        depositJson = "{\"username\": \"${username}\", \"moneyToDeposit\": 0, \"moneyToWithdrawn\": 1400}"
        mvc.perform(post('/wallet/withdrawn')
                .contentType(MediaType.APPLICATION_JSON)
                .content(depositJson))
                .andExpect(status().isOk())

        then: 'user has less money'
        wallet = walletRepository.findWalletByUsername(username).get()
        assert wallet.amountBtc == 0d
        assert wallet.amountEuro == 100d
    }
}
