package blacktokkies.toquiz.helper.token;

import blacktokkies.toquiz.common.error.exception.RestApiException;
import blacktokkies.toquiz.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static blacktokkies.toquiz.common.error.errorcode.AuthErrorCode.INVALID_REFRESH_TOKEN;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${application.security.jwt.access-token.expiration}")
    private Integer ACCESS_TOKEN_EXPIRATION;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer REFRESH_TOKEN_EXPIRATION;
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getSubject(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public String generateRefreshToken(String subject){
        return generateToken(new HashMap<>(), subject, REFRESH_TOKEN_EXPIRATION);
    }

    public String generateAccessToken(String subject){
        return generateToken(new HashMap<>(), subject, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateToken(
        Map<String, Object> extraClaims,
        String subject,
        int tokenExpiration
    ){
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String refreshAccessToken(String refreshToken, Member member){
        if(!isTokenValid(refreshToken, member))
            throw new RestApiException(INVALID_REFRESH_TOKEN);
        return generateAccessToken(member.getEmail());
    }
}
