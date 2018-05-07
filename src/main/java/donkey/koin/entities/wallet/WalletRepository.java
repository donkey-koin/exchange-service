package donkey.koin.entities.wallet;

import donkey.koin.entities.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w JOIN User u ON u.id = w.userId WHERE u.username = :username")
    Optional<Wallet> findWalletByUsername(@Param("username") String username);
    Optional<Wallet> findWalletByUserId(User user);

    @Query("UPDATE Wallet SET amountBtc = amountBtc + :amount WHERE (" +
            "SELECT u.id " +
            "FROM User u JOIN Wallet w ON (w.userId = u.id)) = :username")
    void addAmountBtc(@Param("amount") int amount, @Param("username") String username);
}
