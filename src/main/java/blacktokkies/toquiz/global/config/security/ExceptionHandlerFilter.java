package blacktokkies.toquiz.global.config.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.INVALID_ACCESS_TOKEN;
import static blacktokkies.toquiz.global.config.security.ResponseWriter.sendErrorResponse;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch(UsernameNotFoundException | JwtException e){
            sendErrorResponse(response, INVALID_ACCESS_TOKEN);
        }
    }
}
