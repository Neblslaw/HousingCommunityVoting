package pl.lodz.p.it.mateusz.gielczynski.VoteService.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class JWSHelper {

    @Value("${app.jws.key}")
    private String secret;

    private static String generateHMACSignature(String message, String secret) {
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            byte[] signatureBytes = hmacSHA256.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            log.error("Failed to generate jws signature", e);
            throw new RuntimeException(e);
        }
    }

    public String generateSignature(String message) {
        return generateHMACSignature(message, secret);
    }
}
