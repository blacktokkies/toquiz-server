package blacktokkies.toquiz.global.common.annotation.activeInfoId;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("application.security.cookie.active-info-id")
@Getter @Setter
public class ActiveInfoIdExpiration {
    private Integer expiration;
}
