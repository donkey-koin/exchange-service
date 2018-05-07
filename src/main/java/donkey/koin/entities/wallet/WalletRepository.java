package donkey.koin.entities.wallet;

import donkey.koin.entities.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    Optional<Wallet> findWalletByUserId(User user);

}
