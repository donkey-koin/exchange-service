package donkey.koin.entities.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACTIONS")
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_TYPE")
    private TransactionType transactionType;

    @Column(name = "TRANSACTION_TIMESTAMP")
    private Instant transactionTimeStamp;

    @Column(name = "AMOUNT_BTC")
    private Double donkeyKoinAmount;

    @Column(name = "AMOUNT_EURO")
    private Double euroAmount;
}
