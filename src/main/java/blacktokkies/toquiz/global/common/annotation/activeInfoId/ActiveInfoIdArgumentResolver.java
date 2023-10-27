package blacktokkies.toquiz.global.common.annotation.activeInfoId;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.auth.jwt.JwtTokenProvider;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.global.common.error.AuthenticationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActiveInfoIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final ActiveInfoRepository activeInfoRepository;
    private final ActiveInfoIdExpiration activeInfoIdExpiration;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    final String ActiveInfoIdCookieName ="active_info_id";
    final String GENERATE_NEW_ACTIVE_INFO = "GENERATE_NEW_ACTIVE_INFO";

    private final int TOKEN_INDEX = 1;

    @Override
    public boolean supportsParameter(MethodParameter parameter){
        return parameter.hasParameterAnnotation(ActiveInfoId.class)
            & parameter.getParameterType().equals(ActiveInfoIdDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        Cookie activeInfoIdCookie = getActiveInfoIdCookie(request);
        response.addCookie(activeInfoIdCookie);

        return new ActiveInfoIdDto(activeInfoIdCookie.getValue());
    }

    // Cookie에 ActiveInfoId가 존재하는지 확인, 그렇지 않으면 새로 발급
    private Cookie getActiveInfoIdCookie(HttpServletRequest request){
        return Arrays.stream(request.getCookies())
            .filter(cookie -> ActiveInfoIdCookieName.equals(cookie.getName()))
            .findAny()
            .orElseGet(() -> generateActiveInfoId(request));
    }

    // 새로운 ActiveInfoId를 발급
    private Cookie generateActiveInfoId(HttpServletRequest request){
        String activeInfoId = getActiveInfoByMember(request);

        if(activeInfoId.equals(GENERATE_NEW_ACTIVE_INFO)) {
            ActiveInfo activeInfo = activeInfoRepository.save(new ActiveInfo());
            activeInfoId = activeInfo.getId();
        }

        Cookie cookie = new Cookie("active_info_id", activeInfoId);
        cookie.setMaxAge(activeInfoIdExpiration.getExpiration());
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        log.info("ActiveInfoId 발급");
        return cookie;
    }

    private String getActiveInfoByMember(HttpServletRequest request){
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            String jwtToken = authorization.split(" ")[TOKEN_INDEX];
            String email = jwtTokenProvider.extractEmailFromAccessToken(jwtToken);

            return memberRepository.findActiveInfoIdByEmail(email).orElse(GENERATE_NEW_ACTIVE_INFO);
        }catch(AuthenticationException | NullPointerException e){
            return GENERATE_NEW_ACTIVE_INFO;
        }
    }
}
