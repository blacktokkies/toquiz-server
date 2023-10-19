package blacktokkies.toquiz.member.api;

import blacktokkies.toquiz.domain.member.api.AuthApi;
import blacktokkies.toquiz.domain.member.application.AuthService;
import blacktokkies.toquiz.domain.member.dto.request.LoginRequest;
import blacktokkies.toquiz.domain.member.dto.request.SignUpRequest;
import blacktokkies.toquiz.domain.member.dto.response.AuthenticateResponse;
import blacktokkies.toquiz.domain.model.Provider;
import blacktokkies.toquiz.global.common.error.RestApiException;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import blacktokkies.toquiz.global.util.auth.CookieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.*;
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
    @Mock
    private CookieService cookieService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc; // HTTP 호출을 위해 사용

    final private String accessToken = "eyJhbGciOiJIUzI1NiJ9" +
        ".eyJzdWIiOiJzb29tYW4zMTFAbmF2ZXIuY29tIiwiaWF0IjoxNjkzMjMyMDMyLCJleHAiOjE2OTM0MDQ4MzJ9.TiElWh1AX3T7Rb_Q2g4izZXMiVy_kpFiacEkNnhpNdE";

    final private String refreshToken = "eyJhbGciOiJIUzI1NiJ9" +
        ".eyJzdWIiOiJzb29tYW4zMTExMTExMUBuYXZlci5jb20iLCJpYXQiOjE2OTc2ODk4NjIsImV4cCI6MTY5ODk4NTg2Mn0" +
        ".GFVVNyavB80mjhgFi9dIwpRbiKEWIKVFiJHLi_eitgI";

    final private String activeInfoId = "6530b174557a982d57909ab3";

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authApi).build();
    }

    private AuthenticateResponse authenticationResponse() {
        return AuthenticateResponse.builder()
            .id(1L)
            .email("test311@naver.com")
            .provider(Provider.LOCAL)
            .nickname("TEST")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .accessToken(accessToken)
            .build();
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
        void 회원가입_성공() throws Exception {
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
        class 회원가입_실패 {

            @Nested
            class 형식_제약_검증_실패 {
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

    @Nested
    @DisplayName("로그인")
    class Login {
        private ResultActions requestApi(LoginRequest request) throws Exception {
            return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            );
        }

        @Test
        void 로그인_성공() throws Exception {
            // given
            final LoginRequest request = LoginRequest.builder()
                .email("test311@naver.com")
                .password("test@311")
                .build();

            final AuthenticateResponse response = authenticationResponse();

            doReturn(response).when(authService).login(any(LoginRequest.class));
            doReturn(new Cookie("refresh_token", refreshToken)).when(cookieService).issueRefreshTokenCookie(any(String.class));
            doReturn(new Cookie("active_info_id", activeInfoId)).when(cookieService).issueActiveInfoIdCookie(any(String.class));

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(new SuccessResponse<>(response).getStatusCode()))
                .andReturn();
        }

        @Nested
        class 로그인_실패 {
            @Nested
            class 형식_제약_검증_실패 {
                @Test
                void 비밀번호_형식_제약_검증() throws Exception {
                    final LoginRequest request = LoginRequest.builder()
                        .email("test311@naver.com")
                        .password("test11311")
                        .build();

                    // when
                    final ResultActions resultActions = requestApi(request);

                    // then
                    resultActions.andExpect(status().isBadRequest()).andReturn();
                }

                @Test
                void 비밀번호_길이_제약_검증() throws Exception {
                    final LoginRequest request1 = LoginRequest.builder() // 길이가 8자 미만
                        .email("test311@naver.com")
                        .password("test@12")
                        .build();

                    final LoginRequest request2 = LoginRequest.builder() // 길이가 20자 초과
                        .email("test311@naver.com")
                        .password("test@012345678012345678")
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
                    final LoginRequest request = LoginRequest.builder()
                        .email("test311")
                        .password("test11311")
                        .build();

                    // when
                    final ResultActions resultActions = requestApi(request);

                    // then
                    resultActions.andExpect(status().isBadRequest()).andReturn();
                }
            }

            @Test
            void 아이디가_존재하지_않음() {
                // given
                final LoginRequest request = LoginRequest.builder()
                    .email("test311@naver.com")
                    .password("test@311")
                    .build();

                doThrow(new RestApiException(NOT_EXIST_MEMBER)).when(authService).login(any(LoginRequest.class));
                ;

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(NOT_EXIST_MEMBER));
            }

            @Test
            void 비밀번호가_일치하지_않음() {
                // given
                final LoginRequest request = LoginRequest.builder()
                    .email("test311@naver.com")
                    .password("test@311")
                    .build();

                doThrow(new RestApiException(NOT_MATCH_PASSWORD)).when(authService).login(any(LoginRequest.class));
                ;

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(NOT_MATCH_PASSWORD));
            }
        }
    }
}