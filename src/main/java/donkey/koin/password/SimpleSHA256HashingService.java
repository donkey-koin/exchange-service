package donkey.koin.password;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class SimpleSHA256HashingService implements HashService {

    @Override
    public String generateHashedString(String plainText) {
        return DigestUtils.sha256Hex(plainText);
    }
}
