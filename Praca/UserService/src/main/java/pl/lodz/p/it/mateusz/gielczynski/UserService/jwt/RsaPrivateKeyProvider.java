package pl.lodz.p.it.mateusz.gielczynski.UserService.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty("app.security.jwt.rsa.private-key")
public class RsaPrivateKeyProvider {

    //todo change to path
    @Value("${app.security.jwt.rsa.private-key}")
    private String privateKey;


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
}
