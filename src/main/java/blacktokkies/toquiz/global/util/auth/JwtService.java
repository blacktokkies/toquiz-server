package blacktokkies.toquiz.global.util.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        }
        catch (Exception e){
            jwtExceptionHandler(e);
        }
        return null;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getSubject(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public String generateToken(String subject, int tokenExpiration){
        return generateToken(new HashMap<>(), subject, tokenExpiration);
    }

    public String generateToken(
        Map<String, Object> extraClaims,
        String subject,
        int tokenExpiration
    ) {
        try {
            return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        }catch (Exception e){
            jwtExceptionHandler(e);
        }
        return null;
    }

    private Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private void jwtExceptionHandler(Exception e) {
        if(e instanceof SecurityException){
            throw new JwtException("잘못된 JWT 시그니처");
        }
        if(e instanceof MalformedJwtException){
            throw new JwtException("유효하지 않은 JWT 토큰");
        }
        if(e instanceof ExpiredJwtException){
            throw new JwtException("만료된 JWT 토큰");
        }
        if(e instanceof UnsupportedJwtException){
            throw new JwtException("지원되지 않는 JWT 토큰");
        }
        if(e instanceof IllegalArgumentException){
            throw new JwtException("JWT token compact of handler are invalid");
        }
        throw new JwtException("JWT 체크하지 못한 오류");
    }
}
