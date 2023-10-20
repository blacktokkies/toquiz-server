package blacktokkies.toquiz.config;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.model.Provider;
import blacktokkies.toquiz.global.util.auth.PasswordEncryptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation){
        // Mock Member
        Member member = Member.builder()
            .email("test311@naver.com")
            .password(PasswordEncryptor.encryptPassword("test@311"))
            .nickname("TEST")
            .provider(Provider.LOCAL)
            .activeInfoId("{activeInfoId}")
            .build();

        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        final Authentication auth = new UsernamePasswordAuthenticationToken(member, member.getPassword(),
            member.getAuthorities());

        context.setAuthentication(auth);

        return context;
    }
}
