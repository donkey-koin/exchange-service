package donkey.koin.transactions.transaction;

import donkey.koin.wallets.wallet.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

    @Autowired
    private final WalletService walletService;

    public void purchase(TransactionDetails transactionDetails) {

    }

    public void sell(TransactionDetails transactionDetails) {

    }
}
