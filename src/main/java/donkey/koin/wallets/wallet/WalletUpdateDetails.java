package donkey.koin.wallets.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WalletUpdateDetails {
    private final byte[] publicKey;
    private double donkeyKoin;

    public WalletUpdateDetails(@JsonProperty("publicKey") byte[] publicKey,
                               @JsonProperty("donkeyKoin") double donkeyKoin) {
        this.publicKey = publicKey;
        this.donkeyKoin = donkeyKoin;
    }
//    private final double euroAmount;
}
