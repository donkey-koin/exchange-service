package donkey.koin.wallets.wallet;

import donkey.koin.entities.user.User;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.entities.wallet.Wallet;
import donkey.koin.entities.wallet.WalletRepository;
import donkey.koin.transactions.transaction.InitTransaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

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
        if (!oWallet.isPresent()) {
            throw new HttpClientErrorException(NOT_FOUND, String.format("Don't know how but wallet is not present for user %s", username));
        }
        return oWallet.get();
    }

    public void updateWallets(List<WalletUpdateDetails> walletsToUpdateWithAmount) {
        walletsToUpdateWithAmount
                .stream()
                .filter(this::isNotInitPublicKey)
                .forEach(
                        walletUpdateDetails -> {
                            String username = userRepository.findUserByPublicKey(walletUpdateDetails.getPublicKey())
                                    .map(User::getUsername)
                                    .orElseThrow(() -> new HttpServerErrorException(EXPECTATION_FAILED, "Fail when searching for wallet to update"));

                            walletRepository.findWalletByUsername(username)
                                    .map(wallet -> {
                                        wallet.setAmountBtc(wallet.getAmountBtc() + walletUpdateDetails.getDonkeyKoin());
                                        return walletRepository.save(wallet);
                                    })
                                    .orElseThrow(() -> new HttpServerErrorException(EXPECTATION_FAILED, "Fail when trying to update wallet"));
                        }
                );
    }

    private boolean isNotInitPublicKey(WalletUpdateDetails walletUpdateDetails) {
        return !Arrays.equals(walletUpdateDetails.getPublicKey(), InitTransaction.adminPublicKey);
    }
}
