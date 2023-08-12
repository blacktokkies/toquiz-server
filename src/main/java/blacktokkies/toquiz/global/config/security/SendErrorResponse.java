package blacktokkies.toquiz.global.config.security;

import blacktokkies.toquiz.global.common.error.errorcode.ErrorCode;
import blacktokkies.toquiz.global.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SendErrorResponse {
    public static void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        log.warn("[{}] {}", "AuthenticationEntryPoint", errorCode.name());

        response.setContentType("application/json");
        response.setStatus(errorCode.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");

        ObjectMapper mapper = new ObjectMapper();

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.name())
            .statusCode(errorCode.getHttpStatus().value())
            .message(errorCode.getMessage())
            .build();

        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
