package blacktokkies.toquiz.member.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.member.application.AuthService;
import blacktokkies.toquiz.domain.member.dao.MemberRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks // 가짜 객체 생성
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ActiveInfoRepository activeInfoRepository;

}
