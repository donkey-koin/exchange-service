package donkey.koin.transactions.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;

@Getter
public class InitTransaction {

    private final byte[] publicKey;
    private final double moneyAmount;

    public InitTransaction(@JsonProperty("moneyAmount") Double moneyAmount,
                              @JsonProperty("publicKey") byte[] publicKey) {
        this.moneyAmount = moneyAmount;
        this.publicKey = publicKey;
    }

}
