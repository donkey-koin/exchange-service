package donkey.koin.entities.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByUsernameOrEmail(String username, String email);
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByPublicKey(byte[] publicKeyInBytes);
}
