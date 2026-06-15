package campusmarketplace.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "campusmarketplacejwtsecretkeycampusmarketplacejwtsecretkey";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis()
                                + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        return claims
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("TOKEN = " + token);
            extractEmail(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}