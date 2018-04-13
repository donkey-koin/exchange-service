package donkey.koin.password;

public interface HashService {
    String generateHashedString(String plainText);
}
