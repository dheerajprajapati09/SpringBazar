package in.dk.SpringBazaar.JwtToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.expiration-ms}")
    private long validityInMilliseconds;

    @Value("${jwt.refreshExpiration}")
    private long refreshValidityInMilliseconds;


    private Key secretKey;

    @jakarta.annotation.PostConstruct
    protected void init() {
        if (secretKeyString == null || secretKeyString.isEmpty()) {
            secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Generated new secret key (Base64): " + base64Key);
        } else {
            byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
            secretKey = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().
                    setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return !isTokenExpired(token);

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
