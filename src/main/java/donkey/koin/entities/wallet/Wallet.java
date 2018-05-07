package donkey.koin.entities.wallet;

import donkey.koin.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WALLETS")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(name = "AMOUNT_EURO")
    private Double amountEuro;

    @Column(name = "AMOUNT_BTC")
    private Double amountBtc;

}
