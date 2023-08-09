package blacktokkies.toquiz.helper.token;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("email")
@Getter
public class RefreshToken {
    @Id
    Long id;
    @Indexed
    String email;
    String refreshToken;

    public RefreshToken(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
