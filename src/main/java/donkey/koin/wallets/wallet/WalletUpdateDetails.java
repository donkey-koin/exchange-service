package donkey.koin.wallets.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WalletUpdateDetails {
    private final byte[] publicKey;
    private final double lastKoinValue;
    private double donkeyKoin;

    public WalletUpdateDetails(@JsonProperty("publicKey") byte[] publicKey,
                               @JsonProperty("donkeyKoin") double donkeyKoin,
                               @JsonProperty("lastKoinValue") double lastKoinValue) {
        this.publicKey = publicKey;
        this.donkeyKoin = donkeyKoin;
        this.lastKoinValue = lastKoinValue;
    }
}
