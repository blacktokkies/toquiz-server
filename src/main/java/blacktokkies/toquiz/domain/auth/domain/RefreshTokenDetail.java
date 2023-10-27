package blacktokkies.toquiz.domain.auth.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "refreshToken")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDetail {
    @Id
    private Long id;
    @Indexed
    private String email;
    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Integer expiration;

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public static RefreshTokenDetail of(String email, String refreshToken, int expiration){
        return RefreshTokenDetail.builder()
            .email(email)
            .refreshToken(refreshToken)
            .expiration(expiration)
            .build();
    }
}
