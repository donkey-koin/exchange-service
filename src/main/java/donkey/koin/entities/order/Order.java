package donkey.koin.entities.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ORDER_TYPE")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "OWNER_ID")
    private byte[] ownerId;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

}
