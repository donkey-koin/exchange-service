package donkey.koin.entities.wallet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w JOIN User u ON u.id = w.userId WHERE u.username = :username")
    Optional<Wallet> findWalletByUsername(@Param("username") String username);
}
