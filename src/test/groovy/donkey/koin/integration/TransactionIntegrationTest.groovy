package donkey.koin.integration

import donkey.koin.app.ExchangeApplication
import donkey.koin.entities.transaction.TransactionRepository
import donkey.koin.entities.wallet.WalletRepository
import donkey.koin.users.registration.UserRegistrationDetails
import donkey.koin.users.registration.UserRegistrationService
import org.junit.Before
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

import java.time.Instant

import static donkey.koin.dictionaries.WebServicesDictionary.TRANSACTION
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
class TransactionIntegrationTest {

    @Autowired
    MockMvc mvc

    @Autowired
    TransactionRepository transactionRepository
    @Autowired
    WalletRepository walletRepository

    @Autowired
    UserRegistrationService userRegistrationService

    @Before
    void before() {
        def username = 'szymo080'
        def userDetails = new UserRegistrationDetails(
                username,
                'szymon123',
                'szymo@mail.com'
        )

        userRegistrationService.registerUser(userDetails)
    }

    @Test
    void 'should test purchase transaction'() {
        given:
        def username = 'szymo080'
        def wallet = walletRepository.findWalletByUsername(username).get()
        wallet.amountEuro = 2000d
        walletRepository.save(wallet)

        def transactionDetails = "{" +
                "\"moneyAmount\":1, " +
                "\"username\":\"szymo080\", " +
                "\"lastKoinValue\":1390, " +
                "\"transactionTime\":\"${Instant.now()}\"" +
                "}"
        when:
        mvc.perform(post(TRANSACTION + "/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionDetails))
                .andExpect(status().isOk())

        then:
        def actualWallet = walletRepository.findWalletByUsername(username).get()
        assert actualWallet.amountEuro == 610
        assert actualWallet.amountBtc == 1d

        def transaction = ++transactionRepository.findAll().iterator()
        assert transaction.euroAmount == 1390d
    }

    @Test
    void 'should test purchase fail transaction'() {
        given:
        def username = 'szymo080'
        def wallet = walletRepository.findWalletByUsername(username).get()
        wallet.amountEuro = 1200d
        walletRepository.save(wallet)

        def transactionDetails = "{" +
                "\"moneyAmount\":1, " +
                "\"username\":\"szymo080\", " +
                "\"lastKoinValue\":1390, " +
                "\"transactionTime\":\"${Instant.now()}\"" +
                "}"
        when:
        mvc.perform(post(TRANSACTION + "/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionDetails))
                .andExpect(status().isPaymentRequired())
    }

    @Test
    void 'should test sell transaction'() {
        given:
        def username = 'szymo080'
        def wallet = walletRepository.findWalletByUsername(username).get()
        wallet.amountBtc = 5d
        walletRepository.save(wallet)

        def transactionDetails = "{" +
                "\"moneyAmount\":3, " +
                "\"username\":\"szymo080\", " +
                "\"lastKoinValue\":1390, " +
                "\"transactionTime\":\"${Instant.now()}\"" +
                "}"
        when:
        mvc.perform(post(TRANSACTION + "/sell")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionDetails))
                .andExpect(status().isOk())

        then:
        def actualWallet = walletRepository.findWalletByUsername(username).get()
        assert actualWallet.amountEuro == 4170d
        assert actualWallet.amountBtc == 2d

        def transaction = ++transactionRepository.findAll().iterator()
        assert transaction.euroAmount == 4170d
    }

    @Test
    void 'should test sell fail transaction'() {
        given:
        def username = 'szymo080'
        def wallet = walletRepository.findWalletByUsername(username).get()
        wallet.amountBtc = 2d
        walletRepository.save(wallet)

        def transactionDetails = "{" +
                "\"moneyAmount\":3, " +
                "\"username\":\"szymo080\", " +
                "\"lastKoinValue\":1390, " +
                "\"transactionTime\":\"${Instant.now()}\"" +
                "}"
        when:
        mvc.perform(post(TRANSACTION + "/sell")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionDetails))
                .andExpect(status().isPaymentRequired())
    }
}
