package blacktokkies.toquiz.global.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import static blacktokkies.toquiz.global.common.error.errorcode.CommonErrorCode.ACCESS_DENIED;
import static blacktokkies.toquiz.global.config.security.SendErrorResponse.sendErrorResponse;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException{
        sendErrorResponse(response, ACCESS_DENIED);
    }
}
