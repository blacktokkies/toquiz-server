package blacktokkies.toquiz.global.common.annotation.auth;

import blacktokkies.toquiz.domain.auth.jwt.JwtTokenProvider;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.global.common.error.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.NOT_EXIST_MEMBER;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int TOKEN_INDEX = 1;

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class)
            & parameter.getParameterType().equals(MemberEmailDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory
    ) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken = authorization.split(" ")[TOKEN_INDEX];
        String email = jwtTokenProvider.extractEmailFromAccessToken(jwtToken);
        checkIsExistMember(email);

        return new MemberEmailDto(email);
    }

    private void checkIsExistMember(String email){
        if(!memberRepository.existsByEmail(email)){
            throw new AuthenticationException(NOT_EXIST_MEMBER);
        }
    }
}
