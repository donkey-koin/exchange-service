package donkey.koin.transactions.transaction;

import donkey.koin.entities.transaction.Transaction;
import donkey.koin.entities.transaction.TransactionRepository;
import donkey.koin.entities.transaction.TransactionType;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.entities.wallet.Wallet;
import donkey.koin.wallets.wallet.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    @Autowired
    private final WalletService walletService;

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final UserRepository userRepository;

    public Transaction purchase(TransactionDetails transactionDetails) {
        Wallet currentWallet = walletService.getCurrentWallet(transactionDetails.getUsername());
        double euroAmount = transactionDetails.getMoneyAmount() * transactionDetails.getLastKoinValue();

        if (currentWallet.getAmountEuro() >= euroAmount) {
            calculateMoreBtcs(transactionDetails, currentWallet);
            calculateLessMoney(transactionDetails, currentWallet);
            walletService.updateBtc(currentWallet);

            Transaction transaction = new Transaction();
            transaction.setTransactionTimeStamp(transactionDetails.getTransactionTime());
            transaction.setTransactionType(TransactionType.PURCHASE);
            transaction.setDonkeyKoinAmount(transactionDetails.getMoneyAmount());
            transaction.setEuroAmount(euroAmount);
            transaction.setUserId(userRepository.findUserByUsername(transactionDetails.getUsername()).get().getId());
            transactionRepository.save(transaction);

            log.info("Purchased {} donkey koins for user '{}' for prize of ",
                    transactionDetails.getMoneyAmount(), transactionDetails.getUsername(), transactionDetails.getLastKoinValue());

            return transaction;
        } else {
            log.info("Not enough euros for purchase of '{}' donkey koins for user '{}'",
                    transactionDetails.getMoneyAmount(), transactionDetails.getUsername());
            throw new HttpClientErrorException(HttpStatus.PAYMENT_REQUIRED, "Not enough euro");
        }
    }

    public Transaction sell(TransactionDetails transactionDetails) {
        Wallet currentWallet = walletService.getCurrentWallet(transactionDetails.getUsername());
        if (currentWallet.getAmountBtc() >= transactionDetails.getMoneyAmount()) {
            calculateLessBtcs(transactionDetails, currentWallet);
            calculateMoreMoney(transactionDetails, currentWallet);
            walletService.updateBtc(currentWallet);

            Transaction transaction = new Transaction();
            transaction.setTransactionTimeStamp(transactionDetails.getTransactionTime());
            transaction.setTransactionType(TransactionType.SALE);
            transaction.setDonkeyKoinAmount(transactionDetails.getMoneyAmount());
            transaction.setEuroAmount(transactionDetails.getMoneyAmount() * transactionDetails.getLastKoinValue());
            transaction.setUserId(userRepository.findUserByUsername(transactionDetails.getUsername()).get().getId());
            transactionRepository.save(transaction);

            log.info("Sold {} donkey koins for user '{}' for prize of ",
                    transactionDetails.getMoneyAmount(), transactionDetails.getUsername(), transactionDetails.getLastKoinValue());

            return transaction;
        } else {
            log.info("Not enough koins for sale of '{}' donkey koins for user '{}'",
                    transactionDetails.getMoneyAmount(), transactionDetails.getUsername());
            throw new HttpClientErrorException(HttpStatus.PAYMENT_REQUIRED, "Not enough Donkey koins");
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
