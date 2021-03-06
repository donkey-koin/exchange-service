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
import donkey.koin.krypto.MiniTransaction;
import donkey.koin.krypto.PotentialTransaction;
import donkey.koin.wallets.wallet.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    private final WalletService walletService;
    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public PotentialTransaction purchase(TransactionDetails transactionDetails) {
        List<Order> orders = makeTransaction(transactionDetails, TransactionType.PURCHASE);
        return preparePotentialTransaction(transactionDetails, orders);
    }

    public PotentialTransaction sell(TransactionDetails transactionDetails) {
        List<Order> orders = makeTransaction(transactionDetails, TransactionType.SALE);
        return preparePotentialTransaction(transactionDetails, orders);
    }

    public void init(InitTransaction initTransaction) {
        Order order = new Order();
        order.setAmount(initTransaction.getMoneyAmount());
        order.setOwnerId(initTransaction.getPublicKey());
        order.setOrderType(OrderType.SELL);
        order.setTimestamp(LocalDateTime.now());
        orderRepository.save(order);
    }

    private PotentialTransaction preparePotentialTransaction(TransactionDetails transactionDetails, List<Order> orders) {
        List<MiniTransaction> miniTransactions = new ArrayList<>(orders.size());
        User recipient = userRepository.findUserByUsername(transactionDetails.getUsername()).get();
        orders.forEach(order -> miniTransactions.add(new MiniTransaction(order.getOwnerId(), order.getAmount())));
        return new PotentialTransaction(miniTransactions, recipient.getPublicKey(), transactionDetails.getMoneyAmount(), transactionDetails.getLastKoinValue());
    }

    private List<Order> makeTransaction(TransactionDetails transactionDetails, TransactionType transactionType) {
        Wallet currentWallet = walletService.getCurrentWallet(transactionDetails.getUsername());
        double coinsToTransact = transactionDetails.getMoneyAmount();

        if (checkIfEnoughMoneyInWallet(transactionType, currentWallet, transactionDetails)) {
            User user = userRepository.findUserByUsername(transactionDetails.getUsername()).get();

            checkIfUserHasEnoughMoneyToPlaceNewOrder(currentWallet, transactionDetails, transactionType, user);

            List<Order> consumedOrders = collectAvailableOrders(transactionType, coinsToTransact, user.getPublicKey());

            if (consumedOrders.isEmpty()) {
                registerNewOrder(transactionType.equals(TransactionType.PURCHASE) ? OrderType.BUY : OrderType.SELL, coinsToTransact, user.getPublicKey());
                throw new HttpClientErrorException(HttpStatus.INSUFFICIENT_STORAGE, "Not enough coins on " + (transactionType.equals(TransactionType.PURCHASE) ? "sale" : "buy"));
            }
            orderRepository.deleteAll(consumedOrders);
            double consumedValue = consumedOrders.stream().mapToDouble(Order::getAmount).sum();
            transactionDetails.setMoneyAmount(consumedValue);
            registerNewTransaction(transactionDetails, transactionType, consumedValue, user.getId());
            return consumedOrders;
        } else {
            throw new HttpClientErrorException(HttpStatus.PAYMENT_REQUIRED, "Not enough euro");
        }
    }

    private void checkIfUserHasEnoughMoneyToPlaceNewOrder(Wallet wallet, TransactionDetails transactionDetails, TransactionType transactionType, User user) {
        if (transactionType == TransactionType.SALE) {
            double ordersAddedForRequester = orderRepository.findOrderByOwnerId(user.getPublicKey()).stream()
                    .filter(order -> order.getOrderType() == OrderType.SELL)
                    .map(Order::getAmount)
                    .mapToDouble(Double::doubleValue)
                    .sum();

            if (ordersAddedForRequester > transactionDetails.getMoneyAmount()) {
                throw new HttpClientErrorException(HttpStatus.PRECONDITION_FAILED, "Not enough coins to place new order");
            }
        } else {
            double ordersAddedForRequester = orderRepository.findOrderByOwnerId(user.getPublicKey()).stream()
                    .filter(order -> order.getOrderType() == OrderType.BUY)
                    .map(Order::getAmount)
                    .mapToDouble(Double::doubleValue)
                    .sum();

            double neededMoneyToMakeBuyRequest = ordersAddedForRequester * transactionDetails.getLastKoinValue();
            double currentEuroAmount = wallet.getAmountEuro();

            if (neededMoneyToMakeBuyRequest > currentEuroAmount) {
                throw new HttpClientErrorException(HttpStatus.PRECONDITION_FAILED, "Not enough coins to place new order");
            }
        }
    }

    private boolean filterOutRequesterOrders(byte[] publicKey, Order order) {
        return !Arrays.equals(order.getOwnerId(), publicKey);
    }

    private List<Order> collectAvailableOrders(TransactionType transactionType, double coinsToTransact, byte[] publicKey) {
        List<Order> orderList = orderRepository.findOrderByOrderTypeOrderByTimestampAsc(transactionType.equals(TransactionType.PURCHASE) ? OrderType.SELL : OrderType.BUY).stream()
                .filter(order -> filterOutRequesterOrders(publicKey, order))
                .collect(Collectors.toList());

        Double avaliableCoinsInOrders = 0d;
        List<Order> consumedOrders = new LinkedList<>();

        for (Order order : orderList) {
            consumedOrders.add(order);
            avaliableCoinsInOrders += order.getAmount();
            if (avaliableCoinsInOrders >= coinsToTransact) {
                if (avaliableCoinsInOrders > coinsToTransact) {
                    Order partialAmountOrder = new Order();
                    partialAmountOrder.setOrderType(order.getOrderType());
                    partialAmountOrder.setTimestamp(order.getTimestamp());
                    partialAmountOrder.setId(null);
                    partialAmountOrder.setOwnerId(order.getOwnerId());
                    partialAmountOrder.setAmount(order.getAmount() - (avaliableCoinsInOrders - coinsToTransact));

                    order.setAmount(avaliableCoinsInOrders - coinsToTransact);
                    orderRepository.save(order);
                    consumedOrders.remove(consumedOrders.size() - 1);
                    consumedOrders.add(partialAmountOrder);
                }
                return consumedOrders;
            }
        }

        if (avaliableCoinsInOrders != 0) {
            registerNewOrder(transactionType.equals(TransactionType.PURCHASE) ? OrderType.BUY : OrderType.SELL, coinsToTransact - avaliableCoinsInOrders, publicKey);
        }
        return consumedOrders;
    }

    private Transaction registerNewTransaction(TransactionDetails transactionDetails, TransactionType transactionType, Double coins, Long userId) {
        Transaction transaction = new Transaction();
        transaction.setTransactionTimeStamp(transactionDetails.getTransactionTime());
        transaction.setTransactionType(transactionType);
        transaction.setDonkeyKoinAmount(coins);
        transaction.setEuroAmount(coins * transactionDetails.getLastKoinValue());
        transaction.setUserId(userId);
        transactionRepository.save(transaction);
        return transaction;
    }

    private void registerNewOrder(OrderType orderType, double coinsToBuy, byte[] userPublicKey) {
        Order newOrder = new Order();
        newOrder.setOrderType(orderType);
        newOrder.setAmount(coinsToBuy);
        newOrder.setTimestamp(LocalDateTime.now());
        newOrder.setOwnerId(userPublicKey);
        orderRepository.save(newOrder);
    }

    private boolean checkIfEnoughMoneyInWallet(TransactionType transactionType, Wallet wallet, TransactionDetails transactionDetails) {
        if (transactionType.equals(TransactionType.PURCHASE)) {
            return wallet.getAmountEuro() >= transactionDetails.getMoneyAmount() * transactionDetails.getLastKoinValue();
        } else {
            return wallet.getAmountBtc() >= transactionDetails.getMoneyAmount();
        }
    }
}

