package blacktokkies.toquiz.global.config;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.auth.jwt.JwtTokenProvider;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdExpiration;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdArgumentResolver;
import blacktokkies.toquiz.global.common.annotation.auth.AuthArgumentResolver;
import blacktokkies.toquiz.global.common.log.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final ActiveInfoRepository activeInfoRepository;
    private final ActiveInfoIdExpiration activeInfoIdExpiration;
    private final LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(new AuthArgumentResolver(jwtTokenProvider, memberRepository));
        resolvers.add(new ActiveInfoIdArgumentResolver(activeInfoRepository, activeInfoIdExpiration, jwtTokenProvider, memberRepository));
    }
}
