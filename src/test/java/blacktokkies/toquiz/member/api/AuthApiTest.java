package blacktokkies.toquiz.member.api;

import blacktokkies.toquiz.domain.member.api.AuthApi;
import blacktokkies.toquiz.domain.member.application.AuthService;
import blacktokkies.toquiz.domain.member.dto.request.SignUpRequest;
import blacktokkies.toquiz.global.common.error.RestApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.DUPLICATE_EMAIL;
import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.DUPLICATE_NICKNAME;
import static blacktokkies.toquiz.global.common.response.SuccessMessage.SIGN_UP;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthApiTest {
    @InjectMocks // 가짜 객체 주입
    private AuthApi authApi;

    @Mock // 가짜 객체 생성
    private AuthService authService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc; // HTTP 호출을 위해 사용

    @BeforeEach
    public void init(){
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authApi).build();
    }

    @Nested
    @DisplayName("회원 가입")
    class SignUp {
        private ResultActions requestApi(SignUpRequest request) throws Exception {
            return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            );
        }

        @Test
        @DisplayName("회원 가입 성공")
        void 회원가입_성공 () throws Exception {
            // given
            final SignUpRequest request = SignUpRequest.builder()
                .email("test311@naver.com")
                .password("test@311")
                .nickname("TEST")
                .build();

            doNothing().when(authService).signUp(any(SignUpRequest.class));

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(SIGN_UP.getStatusCode()))
                .andExpect(jsonPath("$.message").value(SIGN_UP.getMessage()))
                .andReturn();
        }
        @Nested
        @DisplayName("실패")
        class Fail {

            @Nested
            @DisplayName("형식 제약 검증 실패")
            class ValidationFail {
                @Test
                void 닉네임_길이_제약_검증() throws Exception {
                    final SignUpRequest request1 = SignUpRequest.builder() // 길이가 2자 미만
                        .email("test311@naver.com")
                        .password("test@311")
                        .nickname("T")
                        .build();

                    final SignUpRequest request2 = SignUpRequest.builder() // 길이가 20자 초과
                        .email("test311@naver.com")
                        .password("test@311")
                        .nickname("TEST01234567890123456789")
                        .build();

                    // when
                    final ResultActions resultActions1 = requestApi(request1);
                    final ResultActions resultActions2 = requestApi(request2);

                    // then
                    resultActions1.andExpect(status().isBadRequest()).andReturn();
                    resultActions2.andExpect(status().isBadRequest()).andReturn();
                }

                @Test
                void 비밀번호_형식_제약_검증() throws Exception {
                    final SignUpRequest request = SignUpRequest.builder()
                        .email("test311@naver.com")
                        .password("test11311")
                        .nickname("TEST")
                        .build();

                    // when
                    final ResultActions resultActions = requestApi(request);

                    // then
                    resultActions.andExpect(status().isBadRequest()).andReturn();
                }

                @Test
                void 비밀번호_길이_제약_검증() throws Exception {
                    final SignUpRequest request1 = SignUpRequest.builder() // 길이가 8자 미만
                        .email("test311@naver.com")
                        .password("test@12")
                        .nickname("TEST")
                        .build();

                    final SignUpRequest request2 = SignUpRequest.builder() // 길이가 20자 초과
                        .email("test311@naver.com")
                        .password("test@012345678012345678")
                        .nickname("TEST")
                        .build();

                    // when
                    ResultActions resultActions1 = requestApi(request1);
                    ResultActions resultActions2 = requestApi(request2);

                    // then
                    resultActions1.andExpect(status().isBadRequest()).andReturn();
                    resultActions2.andExpect(status().isBadRequest()).andReturn();
                }

                @Test
                void 아이디_형식_제약_검증() throws Exception {
                    final SignUpRequest request = SignUpRequest.builder()
                        .email("test311")
                        .password("test11311")
                        .nickname("TEST")
                        .build();

                    // when
                    final ResultActions resultActions = requestApi(request);

                    // then
                    resultActions.andExpect(status().isBadRequest()).andReturn();
                }
            }

            @Test
            void 이메일_중복() {
                final SignUpRequest request = SignUpRequest.builder()
                    .email("test311@naver.com")
                    .password("test@311")
                    .nickname("TEST")
                    .build();

                doThrow(new RestApiException(DUPLICATE_EMAIL)).when(authService)
                    .signUp(any(SignUpRequest.class));

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(DUPLICATE_EMAIL));
            }

            @Test
            void 닉네임_중복() {
                final SignUpRequest request = SignUpRequest.builder()
                    .email("test311@naver.com")
                    .password("test@311")
                    .nickname("TEST")
                    .build();

                doThrow(new RestApiException(DUPLICATE_NICKNAME)).when(authService)
                    .signUp(any(SignUpRequest.class));

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(DUPLICATE_NICKNAME));
            }
        }
    }
}