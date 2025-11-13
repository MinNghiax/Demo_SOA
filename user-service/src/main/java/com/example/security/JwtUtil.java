package com.example.security;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "my_super_secret_key";
    private final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    private String base64UrlEncode(String str) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    private String hmacSha256(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(key);
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    public String generateToken(String email) {
        try {
            long exp = System.currentTimeMillis() + EXPIRATION_TIME;

            String header = base64UrlEncode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
            String payload = base64UrlEncode("{\"sub\":\"" + email + "\",\"exp\":" + exp + "}");

            String signature = hmacSha256(header + "." + payload);

            return header + "." + payload + "." + signature;

        } catch (Exception e) {
            throw new RuntimeException("Cannot create token");
        }
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String expectedSig = hmacSha256(parts[0] + "." + parts[1]);
            if (!expectedSig.equals(parts[2])) return false;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            long exp = Long.parseLong(payloadJson.replaceAll(".*\"exp\":(\\d+).*", "$1"));

            return exp > System.currentTimeMillis();

        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        String payload = token.split("\\.")[1];
        String json = new String(Base64.getUrlDecoder().decode(payload));
        return json.replaceAll(".*\"sub\":\"([^\"]+)\".*", "$1");
    }
}
