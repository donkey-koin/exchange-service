package donkey.koin.krypto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotentialTransaction implements Serializable {
    private List<MiniTransaction> usersToTakeMoneyFromToAmount;
    private byte[] recipient;
    private double amount;
    private double lastKoinValue;

    public PotentialTransaction(@JsonProperty("usersToTakeMoneyFromToAmount") List<MiniTransaction> usersToTakeMoneyFromToAmount,
                                @JsonProperty("recipient") byte[] recipient,
                                @JsonProperty("amount") double amount,
                                @JsonProperty("lastKoinValue") double lastKoinValue) {
        this.usersToTakeMoneyFromToAmount = usersToTakeMoneyFromToAmount;
        this.recipient = recipient;
        this.amount = amount;
        this.lastKoinValue = lastKoinValue;
    }

    public double getAmount() {
        return amount;
    }

    public byte[] getRecipient() {
        return recipient;
    }

    @JsonIgnore
    public PublicKey getRecipientPublicKey() {
        return KeyUtil.getRsaPublicKeyKeyFromBytes(recipient);
    }

    @JsonIgnore
    public Map<PublicKey, Double> getOutputsMap() {
        Map<PublicKey, Double> outputs = new HashMap<>();
        usersToTakeMoneyFromToAmount.forEach((transaction) ->
                outputs.put(transaction.getPublicKey(), transaction.getAmount()));
        return outputs;
    }

    public void setUsersToTakeMoneyFromToAmount(List<MiniTransaction> usersToTakeMoneyFromToAmount) {
        this.usersToTakeMoneyFromToAmount = usersToTakeMoneyFromToAmount;
    }

    public void setRecipient(byte[] recipient) {
        this.recipient = recipient;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getLastKoinValue() {
        return lastKoinValue;
    }

    public void setLastKoinValue(double lastKoinValue) {
        this.lastKoinValue = lastKoinValue;
    }
}
