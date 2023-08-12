package blacktokkies.toquiz.helper.token;

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
public class RefreshToken {
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
}
