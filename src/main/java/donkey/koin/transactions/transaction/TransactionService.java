package donkey.koin.transactions.transaction;

import donkey.koin.entities.order.Order;
import donkey.koin.entities.order.OrderRepository;
import donkey.koin.entities.order.OrderType;
import donkey.koin.entities.transaction.Transaction;
import donkey.koin.entities.transaction.TransactionRepository;
import donkey.koin.entities.transaction.TransactionType;
import donkey.koin.entities.user.User;
import donkey.koin.entities.user.UserRepository;
import donkey.koin.entities.wallet.Wallet;
import donkey.koin.wallets.wallet.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    @Autowired
    private final WalletService walletService;

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public Transaction purchase(TransactionDetails transactionDetails) {
        return makeTransaction(transactionDetails,TransactionType.PURCHASE);
    }

    @Transactional
    public Transaction sell(TransactionDetails transactionDetails) {
        return makeTransaction(transactionDetails,TransactionType.SALE);
    }

    private Transaction makeTransaction(TransactionDetails transactionDetails, TransactionType transactionType) {
        Wallet currentWallet = walletService.getCurrentWallet(transactionDetails.getUsername());
        double coinsToBuy = transactionDetails.getMoneyAmount();
        double euroAmount = coinsToBuy * transactionDetails.getLastKoinValue();

        if (currentWallet.getAmountEuro() >= euroAmount) {
            List<Order> orderList = orderRepository.findOrderByOrderTypeOrderByTimestampDesc(transactionType.equals(TransactionType.PURCHASE) ? OrderType.SELL : OrderType.BUY);
            boolean enoughCoinsInOrders = checkOrdersAvailability(orderList, coinsToBuy);
            User user = userRepository.findUserByUsername(transactionDetails.getUsername()).get();
            if (!enoughCoinsInOrders) {
                registerNewOrder(transactionType.equals(TransactionType.PURCHASE) ? OrderType.BUY : OrderType.SELL, coinsToBuy, user.getPublicKey());
                throw new HttpClientErrorException(HttpStatus.INSUFFICIENT_STORAGE, "Not enough coins on sale");
            }
            return registerNewTransaction(transactionDetails, transactionType, euroAmount, user.getId());

        } else {
            throw new HttpClientErrorException(HttpStatus.PAYMENT_REQUIRED, "Not enough euro");
        }
    }

    private boolean checkOrdersAvailability(List<Order> orderList, double coinsToBuy) {
        Double avaliableCoinsInOrders = 0d;
        for (Order order : orderList) {
            if (avaliableCoinsInOrders >= coinsToBuy) {
                if (avaliableCoinsInOrders > coinsToBuy) {
                    order.setAmount(avaliableCoinsInOrders - coinsToBuy);
                    orderRepository.save(order);
                }
                return true;
            }
            avaliableCoinsInOrders += order.getAmount();
        }
        return false;
    }

    private Transaction registerNewTransaction(TransactionDetails transactionDetails, TransactionType transactionType, Double euroAmount, Long userId) {
        Transaction transaction = new Transaction();
        transaction.setTransactionTimeStamp(transactionDetails.getTransactionTime());
        transaction.setTransactionType(transactionType);
        transaction.setDonkeyKoinAmount(transactionDetails.getMoneyAmount());
        transaction.setEuroAmount(euroAmount);
        transaction.setUserId(userId);
        transactionRepository.save(transaction);
        return transaction;
    }

    private void registerNewOrder(OrderType orderType, double coinsToBuy, byte[] userPublicKey) {
        Order newOrder = new Order();
        newOrder.setOrderType(OrderType.BUY);
        newOrder.setAmount(coinsToBuy);
        newOrder.setTimestamp(LocalDateTime.now());
        newOrder.setOwnerId(userPublicKey);
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

