package blacktokkies.toquiz.domain.auth.presentation;

import blacktokkies.toquiz.domain.auth.application.AuthService;
import blacktokkies.toquiz.domain.auth.dto.LoginRequest;
import blacktokkies.toquiz.domain.auth.dto.ResignRequest;
import blacktokkies.toquiz.domain.auth.dto.SignUpRequest;
import blacktokkies.toquiz.domain.auth.dto.AuthenticateResponse;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdDto;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.global.common.error.RestApiException;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import blacktokkies.toquiz.domain.auth.application.CookieService;
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

import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.*;
import static blacktokkies.toquiz.utils.Constants.*;
import static blacktokkies.toquiz.utils.ResponseChecker.checkResultIsAuthenticationResponse;
import static blacktokkies.toquiz.utils.Responses.authenticationResponse;
import static blacktokkies.toquiz.global.common.response.SuccessMessage.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks // 가짜 객체 주입
    private AuthController authApi;

    @Mock // 가짜 객체 생성
    private AuthService authService;
    @Mock
    private CookieService cookieService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc; // HTTP 호출을 위해 사용

    private final Cookie refreshTokenCookie = new Cookie("refresh_token", REFRESH_TOKEN);
    private final Cookie activeInfoIdCookie = new Cookie("active_info_id", ACTIVE_INFO_ID);
    private final Cookie expiredCookie = new Cookie("refresh_token", "cookie.setMaxAge(0)");

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authApi).build();
    }

    @Nested
    @DisplayName("회원 정보 Validation 검증")
    class Validation{
        private ResultActions requestApi(SignUpRequest request) throws Exception {
            return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            );
        }
        @Test
        @DisplayName("닉네임 길이가 2보다 작을 경우 예외를 반환한다")
        void When_NicknameLengthLessThan2_Expect_ThrowException() throws Exception {
            // given
            final SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .password(PW)
                .nickname(SHORT_NICKNAME)
                .build();

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isBadRequest()).andReturn();
        }

        @Test
        @DisplayName("닉네임 길이가 20보다 클 경우 예외를 반환한다")
        void When_NicknameLengthMoreThan20_Expect_ThrowException() throws Exception{
            // given
            final SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .password(PW)
                .nickname(LONG_NICKNAME)
                .build();

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isBadRequest()).andReturn();
        }

        @Test
        @DisplayName("비밀번호 길이가 8보다 작을 경우 예외를 반환한다")
        void When_PasswordLengthLessThen8_Expect_ThrowException() throws Exception {
            // given
            final SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .password(SHORT_PW)
                .nickname(NICKNAME)
                .build();

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isBadRequest()).andReturn();
        }

        @Test
        @DisplayName("비밀번호 길이가 20보다 클 경우 예외를 반환한다")
        void When_PasswordLengthMoreThen20_Expect_ThrowException() throws Exception {
            // given
            final SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .password(LONG_PW)
                .nickname(NICKNAME)
                .build();

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isBadRequest()).andReturn();
        }

        @Test
        @DisplayName("비밀번호에 '특수기호'를 포함하지 않으면 예외를 반환한다")
        void When_PasswordNotIncludeSpecialSymbol_Expect_ThrowException() throws Exception {
            final SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .password(NOT_INCLUDE_SPECIAL_SYMBOL_PW)
                .nickname(NICKNAME)
                .build();

            // when
            ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isBadRequest()).andReturn();
        }

        @Test
        @DisplayName("비밀번호에 '영문자'를 포함하지 않으면 예외를 반환한다")
        void When_PasswordNotIncludeAlphabet_Expect_ThrowException() throws Exception {
            final SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .password(NOT_INCLUDE_ALPHABET_PW)
                .nickname(NICKNAME)
                .build();

            // when
            ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isBadRequest()).andReturn();
        }

        @Test
        @DisplayName("비밀번호에 '숫자'를 포함하지 않으면 예외를 반환한다")
        void When_PasswordNotIncludeNumber_Expect_ThrowException() throws Exception {
            final SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .password(NOT_INCLUDE_NUMBER_PW)
                .nickname(NICKNAME)
                .build();

            // when
            ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isBadRequest()).andReturn();
        }

        @Test
        @DisplayName("아이디가 이메일 형식이 아니면 예외를 반환한다")
        void When_IdIsNotEmail_Expect_ThrowException() throws Exception {
            // given
            final SignUpRequest request = SignUpRequest.builder()
                .email(INVALID_EMAIL)
                .password(PW)
                .nickname(NICKNAME)
                .build();

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isBadRequest()).andReturn();
        }
    }

    @Nested
    @DisplayName("회원가입 로직")
    class SignUp {
        private ResultActions requestApi(SignUpRequest request) throws Exception {
            return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            );
        }

        @Test
        @DisplayName("회원가입 성공 시, Message(200)를 반환한다")
        void When_SignUpSuccess_Expect_ReturnSuccessMessage() throws Exception {
            // given
            final SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .password(PW)
                .nickname(NICKNAME)
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
        @DisplayName("회원가입 실패")
        class SignUpFail {

            @Test
            @DisplayName("이메일 중복시, 예외를 반환한다")
            void When_DuplicateEmail_Expect_ThrowException() {
                // given
                final SignUpRequest request = SignUpRequest.builder()
                    .email(EMAIL)
                    .password(PW)
                    .nickname(NICKNAME)
                    .build();

                doThrow(new RestApiException(DUPLICATE_EMAIL)).when(authService)
                    .signUp(any(SignUpRequest.class));

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(DUPLICATE_EMAIL));
            }

            @Test
            @DisplayName("닉네임 중복시, 예외를 반환한다")
            void Duplicate_Nickname() {
                // given
                final SignUpRequest request = SignUpRequest.builder()
                    .email(EMAIL)
                    .password(PW)
                    .nickname(NICKNAME)
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
        @DisplayName("로그인 성공 시, AuthenticationResponse(200)를 반환한다")
        void When_LoginSuccess_Expect_ReturnAuthenticationResponse() throws Exception {
            // given
            final LoginRequest request = LoginRequest.builder()
                .email(EMAIL)
                .password(PW)
                .build();
            final AuthenticateResponse response = authenticationResponse();

            doReturn(response).when(authService).login(any(LoginRequest.class));
            doReturn(refreshTokenCookie).when(cookieService).issueRefreshTokenCookieByEmail(any(String.class));
            doReturn(activeInfoIdCookie).when(cookieService).issueActiveInfoIdCookieByEmail(any(String.class));

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(new SuccessResponse<>(response).getStatusCode()))
                .andReturn();
            checkResultIsAuthenticationResponse(resultActions);
        }

        @Nested
        @DisplayName("로그인 실패")
        class LoginFail {
            @Test
            @DisplayName("회원 목록에 이메일이 존재하지 않을 시, 'NOT_EXIST_MEMBER' 예외를 반환한다")
            void When_NotExistEmail_Expect_ThrowException() {
                // given
                final LoginRequest request = LoginRequest.builder()
                    .email(EMAIL)
                    .password(PW)
                    .build();

                doThrow(new RestApiException(NOT_EXIST_MEMBER)).when(authService).login(any(LoginRequest.class));

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(NOT_EXIST_MEMBER));
            }

            @Test
            @DisplayName("비밀번호가 일치하지 않을 시, 'NOT_MATCH_PASSWORD' 예외를 반환한다")
            void When_NotMatchPassword_Expect_ThrowException() {
                // given
                final LoginRequest request = LoginRequest.builder()
                    .email(EMAIL)
                    .password(PW)
                    .build();

                doThrow(new RestApiException(NOT_MATCH_PASSWORD)).when(authService).login(any(LoginRequest.class));

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(NOT_MATCH_PASSWORD));
            }
        }
    }

    @Nested
    @DisplayName("로그아웃")
    class Logout {
        private ResultActions requestApi() throws Exception {
            return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/logout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authentication", "Bearer " + ACCESS_TOKEN)
                    .cookie(new Cookie("refresh_token", REFRESH_TOKEN))
                    .cookie(new Cookie("active_info_id", ACTIVE_INFO_ID))
            );
        }

        @Test
        @DisplayName("로그아웃 성공 시, Message(200)을 반환한다")
        void When_LogoutSuccess_Expect_ReturnSuccessMessage() throws Exception {
            // given
            doNothing().when(authService).logout(any(MemberEmailDto.class));
            doReturn(expiredCookie).when(cookieService).expireCookie(any(String.class));

            // when
            final ResultActions resultActions = requestApi();

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(LOGOUT.getStatusCode()))
                .andExpect(jsonPath("$.message").value(LOGOUT.getMessage()));
        }

        @Nested
        @DisplayName("로그아웃 실패")
        class LogoutFail {
            @Test
            @DisplayName("유효하지 않은 액세스 토큰의 경우, 'INVALID_ACCESS_TOKEN' 예외를 반환한다")
            void When_RequestInvalidAccessToken_Expect_ThrowException() {
                // given
                doThrow(new RestApiException(INVALID_ACCESS_TOKEN)).when(authService).logout(any(MemberEmailDto.class));

                // when, then
                assertThatThrownBy(() -> requestApi()).hasCause(new RestApiException(INVALID_ACCESS_TOKEN));
            }
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 갱신")
    class Refresh{
        private ResultActions requestApi() throws Exception {
            return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authentication", "Bearer " + ACCESS_TOKEN)
                    .cookie(new Cookie("refresh_token", REFRESH_TOKEN))
                    .cookie(new Cookie("active_info_id", ACTIVE_INFO_ID))
            );
        }

        @Test
        @DisplayName("리프레시 토큰 갱신 성공 시, AuthenticationResponse를 반환한다")
        void When_RefreshTokenRenewSuccess_Expect_ReturnAuthenticationResponse() throws Exception {
            // given
            final AuthenticateResponse response = authenticationResponse();

            doReturn(response).when(authService).refresh(any(String.class));
            doReturn(refreshTokenCookie).when(cookieService).issueRefreshTokenCookieByEmail(any(String.class));

            // when
            final ResultActions resultActions = requestApi();

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(new SuccessResponse<>(response).getStatusCode()))
                .andReturn();
            checkResultIsAuthenticationResponse(resultActions);
        }

        @Nested
        @DisplayName("리프레시 토큰 갱신 실패")
        class RefreshFail{
            @Test
            @DisplayName("리프레시 토큰에 해당하는 멤버가 존재하지 않을 시, NOT_EXIST_MEMBER 예외를 반환한다")
            void When_NotExistRefreshTokenMember_Expect_ThrowException(){
                // given
                doThrow(new RestApiException(NOT_EXIST_MEMBER)).when(authService).refresh(any(String.class));

                // when, then
                assertThatThrownBy(() -> requestApi()).hasCause(new RestApiException(NOT_EXIST_MEMBER));
            }

            @Test
            @DisplayName("유효하지 않은 리프레시 토큰 요청 시, INVALID_REFRESH_TOKEN 예외를 반환한다")
            void When_RequestInvalidRefreshToken_Expect_ThrowException(){
                // given
                doThrow(new RestApiException(INVALID_REFRESH_TOKEN)).when(authService).refresh(any(String.class));

                // when, then
                assertThatThrownBy(() -> requestApi()).hasCause(new RestApiException(INVALID_REFRESH_TOKEN));
            }
        }
    }

    @Nested
    @DisplayName("회원 탈퇴")
    class Resign{
        private ResultActions requestApi(ResignRequest request) throws Exception {
            return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/resign")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authentication", "Bearer " + ACCESS_TOKEN)
                    .cookie(new Cookie("refresh_token", REFRESH_TOKEN))
                    .cookie(new Cookie("active_info_id", ACTIVE_INFO_ID))
            );
        }
        @Test
        @DisplayName("회원 탈퇴 성공 시, Message(200)를 반환한다")
        void When_ResignSuccess_Expect_ReturnSuccessMessage() throws Exception {
            // given
            final ResignRequest request = ResignRequest.builder()
                .password(PW)
                .build();

            doNothing().when(authService).resign(any(MemberEmailDto.class), any(String.class), any(ActiveInfoIdDto.class));
            doReturn(expiredCookie).when(cookieService).expireCookie(any(String.class));

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(RESIGN.getStatusCode()))
                .andExpect(jsonPath("$.message").value(RESIGN.getMessage()));
        }

        @Nested
        @DisplayName("회원 탈퇴 실패")
        class ResignFail{
            @Test
            @DisplayName("비밀번호가 일치하지 않을 시, NOT_MATCH_PASSWORD 예외를 반환한다")
            void When_PasswordNotMatch_Expect_ThrowException(){
                // given
                ResignRequest request = ResignRequest.builder()
                    .password(PW)
                    .build();

                doThrow(new RestApiException(NOT_MATCH_PASSWORD)).when(authService)
                    .resign(any(MemberEmailDto.class), any(String.class), any(ActiveInfoIdDto.class));

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(NOT_MATCH_PASSWORD));
            }
        }
    }
}