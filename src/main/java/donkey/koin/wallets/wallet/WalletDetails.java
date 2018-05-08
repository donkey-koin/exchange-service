package donkey.koin.wallets.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class WalletDetails {

    @NotNull
    @NotBlank
    private final String username;

    private final Double moneyToWithdrawn;
    private final Double moneyToDeposit;

    public WalletDetails(@JsonProperty("username") String username,
                         @JsonProperty("moneyToDeposit") Double moneyToDeposit,
                         @JsonProperty("moneyToWithdrawn") Double moneyToWithdrawn) {
        this.username = username;
        this.moneyToWithdrawn = moneyToWithdrawn;
        this.moneyToDeposit = moneyToDeposit;
    }
}
