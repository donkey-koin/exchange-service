package donkey.koin.krypto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.PublicKey;

public class MiniTransaction {
    private byte[] publicKeyInBytes;
    private double amount;

    public MiniTransaction(@JsonProperty("publicKey") byte[] publicKeyInBytes,
                           @JsonProperty("amount") double amount) {
        this.publicKeyInBytes = publicKeyInBytes;
        this.amount = amount;
    }

    public byte[] getPublicKeyInBytes() {
        return publicKeyInBytes;
    }

    @JsonIgnore
    public PublicKey getPublicKey() {
        return KeyUtil.getRsaPublicKeyKeyFromBytes(publicKeyInBytes);
    }

    public double getAmount() {
        return amount;
    }
}
