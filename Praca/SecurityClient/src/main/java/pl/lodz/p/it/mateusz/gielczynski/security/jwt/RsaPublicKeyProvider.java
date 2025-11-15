package pl.lodz.p.it.mateusz.gielczynski.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Component
@RequiredArgsConstructor
@ConditionalOnProperty("app.security.jwt.rsa.public-key")
public class RsaPublicKeyProvider {

    //todo change to path
    @Value("${app.security.jwt.rsa.public-key}")
    private String publicKey;

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
