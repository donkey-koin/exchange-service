package donkey.koin.wallets.wallet;

import donkey.koin.entities.wallet.Wallet;
import donkey.koin.entities.wallet.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@Slf4j
@Service
@AllArgsConstructor
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet getCurrentWallet(String username) {
        return getUserWallet(username);
    }

    public void deposit(String username, Double moneyToDeposit) {
        Wallet wallet = getUserWallet(username);
        wallet.setAmountEuro(wallet.getAmountEuro() + moneyToDeposit);
        walletRepository.save(wallet);
    }

    public void withdrawn(String username, Double moneyToWithdrawn) {
        Wallet wallet = getUserWallet(username);
        if (moneyToWithdrawn > wallet.getAmountEuro()) {
            throw new HttpClientErrorException(PRECONDITION_FAILED, "Not enough money");
        }
        wallet.setAmountEuro(wallet.getAmountEuro() - moneyToWithdrawn);
        walletRepository.save(wallet);
    }

    private Wallet getUserWallet(String username) {
        Optional<Wallet> oWallet = walletRepository.findWalletByUsername(username);
        if(!oWallet.isPresent()) {
            throw new HttpClientErrorException(NOT_FOUND, String.format("Don't know how but wallet is not present for user %s", username));
        }
        return oWallet.get();
    }

    public void updateBtc(Wallet wallet) {
        walletRepository.save(wallet);
    }
}
