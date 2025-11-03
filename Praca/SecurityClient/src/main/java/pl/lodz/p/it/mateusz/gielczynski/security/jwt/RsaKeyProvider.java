package pl.lodz.p.it.mateusz.gielczynski.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Component
@RequiredArgsConstructor
public class RsaKeyProvider {

    @Value("""
            ${jwt.rsa.private-key:-----BEGIN PRIVATE KEY-----
            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
            M7nWQcTaqLsh4rII7uDUILRYTqWFRA==
            -----END PRIVATE KEY-----}
            """)
    private String privateKey;

    @Value("""
            ${jwt.rsa.public-key:-----BEGIN PUBLIC KEY-----
            MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEApr9oJKq1eH5DbET0zY5p
            msJmrggCwDKsDmXYHUw2HO5nu+Cd3UOLDgaZqyY/SiqZ+OPBq0lDCdS9CUqTARRB
            zjQhaId2Ma7gHDkmO99UP1GT3lMlSXe8yW+H7FExWulnT99nQNNsU1ML7+J+enj7
            besAwRW8MjnD1LLMnL2FAjFHR0jeBAm0Wm9QB4JuzWRNL5j81MMah2Ijgg2A3RSG
            vfLLxk0WMyJI6fP6CKadZh2LIb3SSq+N2g6zS4C3kC1Yt+sXjcGxcCdAANPpbHs0
            mYsqVvZPUkuM+UFD5kaBWMK0m/LTmqUsAUJy3CgeL1sSIr4q0TLnqoUKgHLh9pue
            WLpe6/VJqr8pE7yPgq+Q7LdRB9secH8WOxvQ11b4kaB/E9LVUBoPjZzvEtnOJqbg
            imb+OD6xihlZ0pGksRY1C7+X6DV5MSJMj2wiY2bdqXhrW2LZy8pPc76ytV7i99Jg
            lPlxLHHvDMx/3XkBV9qvwjNTnf5TdyoJmFb14pleyNgNCpS8soVgYg0706ke6BnR
            whvP8VwD1Z+xgsmrNW47bLoRC+RB3hbt/yxscpzO9AIjjLbaeX3OFGO3MQ3WIVcj
            VtYTC/3PZDD3hk9Kuh5kAQUZobPxjEZKJChsaUsk3qqRLS1s0pQ16QfvYY6ftvZX
            mXt1c8l/pyXeqHmBT72kVdECAwEAAQ==
            -----END PUBLIC KEY-----}
            """)
    private String publicKey;

    @Bean
    public PrivateKey getPrivateKeyFromString() throws Exception {
        String key = privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    @Bean
    public RSAPublicKey getPublicKeyFromString() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String key = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }


}

