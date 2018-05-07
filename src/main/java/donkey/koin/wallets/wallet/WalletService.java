package donkey.koin.wallets.wallet;

import donkey.koin.entities.user.User;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.entities.wallet.Wallet;
import donkey.koin.entities.wallet.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public Wallet getCurrentWallet(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (!user.isPresent()) {
            throw new HttpClientErrorException(NOT_FOUND, String.format("User %s not found", username));
        }

        Optional<Wallet> wallet = walletRepository.findWalletByUserId(user.get());
        if(!wallet.isPresent()) {
            throw new HttpClientErrorException(NOT_FOUND, String.format("Don't know how but wallet is not present for user %s", username));
        }
        return wallet.get();
    }

}
