package donkey.koin.transactions.transaction;

import donkey.koin.entities.wallet.Wallet;
import donkey.koin.wallets.wallet.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    @Autowired
    private final WalletService walletService;

    public void purchase(TransactionDetails transactionDetails) {
        Wallet currentWallet = walletService.getCurrentWallet(transactionDetails.getUsername());
        if (currentWallet.getAmountEuro() <= transactionDetails.getMoneyAmount()) {
            walletService.purchaseBtc(transactionDetails.getMoneyAmount(), transactionDetails.getUsername());
        } else {
            log.info("Not enough euros for purchase of '{}' donkey koins for user '{}'",
                    transactionDetails.getMoneyAmount(), transactionDetails.getUsername());
        }
    }

    public void sell(TransactionDetails transactionDetails) {

    }
}

