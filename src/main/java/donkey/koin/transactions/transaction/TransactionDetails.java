package donkey.koin.transactions.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;

@Getter
public class TransactionDetails {

    private final String username;
    private final Instant transactionTime;
    private final Double moneyAmount;
    private final double lastKoinValue;

    public TransactionDetails(@JsonProperty("moneyAmount") Double moneyAmount,
                              @JsonProperty("username") String username,
                              @JsonProperty("lastKoinValue") double lastKoinValue,
                              @JsonProperty("transactionTime") Instant transactionTime) {
        this.moneyAmount = moneyAmount;
        this.username = username;
        this.lastKoinValue = lastKoinValue;
        this.transactionTime = transactionTime;
    }
}
