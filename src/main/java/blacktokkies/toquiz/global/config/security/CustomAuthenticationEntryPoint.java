package blacktokkies.toquiz.global.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.INVALID_ACCESS_TOKEN;
import static blacktokkies.toquiz.global.common.error.errorcode.CommonErrorCode.HANDLER_NOT_FOUND;
import static blacktokkies.toquiz.global.config.security.ResponseWriter.sendErrorResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HttpRequestEndpointChecker endpointChecker;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if(!endpointChecker.isEndpointExist(request)){
            sendErrorResponse(response, HANDLER_NOT_FOUND);
            return;
        }
        sendErrorResponse(response, INVALID_ACCESS_TOKEN);
    }
}
