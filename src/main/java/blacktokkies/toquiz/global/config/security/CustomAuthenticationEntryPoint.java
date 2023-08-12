package blacktokkies.toquiz.global.config.security;

import blacktokkies.toquiz.global.common.error.errorcode.ErrorCode;
import blacktokkies.toquiz.global.common.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.INVALID_ACCESS_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException  {
        log.warn("Invalid User Request" + request.getRequestURI());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(makeErrorResponse(INVALID_ACCESS_TOKEN));
    }

    private String makeErrorResponse(ErrorCode errorCode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.name())
            .statusCode(errorCode.getHttpStatus().value())
            .message(errorCode.getMessage())
            .build();

        return mapper.writeValueAsString(errorResponse);
    }
}
