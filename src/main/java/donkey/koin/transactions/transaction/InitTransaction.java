package donkey.koin.transactions.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class InitTransaction {

    private final byte[] publicKey;
    private final double moneyAmount;
    public static byte[] adminPublicKey;

    public InitTransaction(@JsonProperty("moneyAmount") Double moneyAmount,
                           @JsonProperty("publicKey") byte[] publicKey) {
        this.moneyAmount = moneyAmount;
        this.publicKey = publicKey;
        adminPublicKey = publicKey;
    }

}
