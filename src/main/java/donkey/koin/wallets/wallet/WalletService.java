package donkey.koin.wallets.wallet;

import donkey.koin.entities.user.UserRepository;
import donkey.koin.entities.wallet.Wallet;
import donkey.koin.entities.wallet.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@AllArgsConstructor
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public Wallet getCurrentWallet(String username) {
        Optional<Wallet> wallet = walletRepository.findWalletByUsername(username);
        if (!wallet.isPresent()) {
            throw new HttpClientErrorException(NOT_FOUND, String.format("Don't know how but wallet is not present for user %s", username));
        }
        return wallet.get();
    }

    public void updateBtc(Wallet wallet) {
        walletRepository.save(wallet);
    }
}
