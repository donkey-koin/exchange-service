package donkey.koin.transactions.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TransactionDetails {

    private final int amount;
    private final String username;

    public TransactionDetails(@JsonProperty("amount") int amount,
                              @JsonProperty("username") String username) {
        this.amount = amount;
        this.username = username;
    }
}
