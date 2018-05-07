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
        if (currentWallet.getAmountEuro() >= transactionDetails.getMoneyAmount()) {
            calculateMoreBtcs(transactionDetails, currentWallet);
            calculateLessMoney(transactionDetails, currentWallet);
            walletService.updateBtc(currentWallet);
            log.info("Purchased {} donkey koins for user '{}' for prize of ",
                    transactionDetails.getMoneyAmount(), transactionDetails.getUsername(), transactionDetails.getLastKoinValue());
        } else {
            log.info("Not enough euros for purchase of '{}' donkey koins for user '{}'",
                    transactionDetails.getMoneyAmount(), transactionDetails.getUsername());
        }
    }

    public void sell(TransactionDetails transactionDetails) {
        Wallet currentWallet = walletService.getCurrentWallet(transactionDetails.getUsername());
        if (currentWallet.getAmountBtc() >= transactionDetails.getMoneyAmount()) {
            calculateLessBtcs(transactionDetails, currentWallet);
            calculateMoreMoney(transactionDetails, currentWallet);
            walletService.updateBtc(currentWallet);
            log.info("Sold {} donkey koins for user '{}' for prize of ",
                    transactionDetails.getMoneyAmount(), transactionDetails.getUsername(), transactionDetails.getLastKoinValue());
        } else {
            log.info("Not enough euros for sale of '{}' donkey koins for user '{}'",
                    transactionDetails.getMoneyAmount(), transactionDetails.getUsername());
        }
    }

    private void calculateLessMoney(TransactionDetails transactionDetails, Wallet currentWallet) {
        currentWallet.setAmountEuro(currentWallet.getAmountEuro() - transactionDetails.getMoneyAmount() * transactionDetails.getLastKoinValue());
    }

    private void calculateMoreBtcs(TransactionDetails transactionDetails, Wallet currentWallet) {
        currentWallet.setAmountBtc(currentWallet.getAmountBtc() + transactionDetails.getMoneyAmount());
    }

    private void calculateMoreMoney(TransactionDetails transactionDetails, Wallet currentWallet) {
        currentWallet.setAmountEuro(currentWallet.getAmountEuro() + transactionDetails.getMoneyAmount() * transactionDetails.getLastKoinValue());
    }

    private void calculateLessBtcs(TransactionDetails transactionDetails, Wallet currentWallet) {
        currentWallet.setAmountBtc(currentWallet.getAmountBtc() - transactionDetails.getMoneyAmount());
    }
}

