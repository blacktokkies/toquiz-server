package blacktokkies.toquiz.domain.member.presentation;

import blacktokkies.toquiz.domain.member.application.MemberService;
import blacktokkies.toquiz.domain.member.dto.UpdateMyInfoRequest;
import blacktokkies.toquiz.domain.member.dto.MemberInfoResponse;
import blacktokkies.toquiz.domain.member.domain.Provider;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.global.common.error.RestApiException;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.INVALID_ACCESS_TOKEN;
import static blacktokkies.toquiz.utils.Constants.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    @InjectMocks
    private MemberController memberApi;

    @Mock
    private MemberService memberService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(memberApi).build();
    }

    @Nested
    @DisplayName("자신의 사용자 정보 가져오기")
    class GetMyInfo{
        private ResultActions requestApi() throws Exception {
            return mockMvc.perform(
                MockMvcRequestBuilders.get("/api/members/me")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + ACCESS_TOKEN)
            );
        }

        @Test
        @DisplayName("자신의 사용자 정보 가져오기 성공 시, MemberInfoResponse(200)를 반환한다")
        void When_GetMyInfoSuccess_Expect_ReturnMemberInfoResponse() throws Exception {
            // given
            final MemberInfoResponse response = MemberInfoResponse.builder()
                .id(1L)
                .email(EMAIL)
                .provider(Provider.LOCAL)
                .nickname(NICKNAME)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            doReturn(response).when(memberService).getMyInfo(any(MemberEmailDto.class));

            // when
            final ResultActions resultActions = requestApi();

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(new SuccessResponse<>(response).getStatusCode()))
                .andReturn();
        }

        @Nested
        @DisplayName("자신의 사용자 정보 가져오기 실패")
        class GetMyInfoFail{
            @Test
            @DisplayName("유효하지 않은 액세스 토큰의 경우, 'INVALID_ACCESS_TOKEN' 예외를 반환한다")
            void When_RequestInvalidAccessToken_Expect_ThrowException(){
                // given
                doThrow(new RestApiException(INVALID_ACCESS_TOKEN)).when(memberService).getMyInfo(any(MemberEmailDto.class));

                // when, then
                assertThatThrownBy(() -> requestApi()).hasCause(new RestApiException(INVALID_ACCESS_TOKEN));
            }
        }
    }
    @Nested
    @DisplayName("자신의 사용자 정보 수정하기")
    class UpdateMyInfo{
        private ResultActions requestApi(UpdateMyInfoRequest request) throws Exception {
            return mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/members/me")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", "Bearer " + ACCESS_TOKEN)
            );
        }
        @Test
        @DisplayName("자신의 사용자 정보 수정하기 성공 시, MemberInfoResponse(200)을 반환한다")
        void When_UpdateMyInfoSuccess_Expect_ReturnMemberInfoResponse() throws Exception {
            // given
            final UpdateMyInfoRequest request = UpdateMyInfoRequest.builder()
                .password(PW)
                .nickname(MODIFY_NICKNAME)
                .build();

            final MemberInfoResponse response = MemberInfoResponse.builder()
                .id(1L)
                .email(EMAIL)
                .provider(Provider.LOCAL)
                .nickname(MODIFY_NICKNAME)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            doReturn(response).when(memberService).updateMyInfo(any(MemberEmailDto.class), any(UpdateMyInfoRequest.class));

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(new SuccessResponse<>(response).getStatusCode()))
                .andExpect(jsonPath("$.result.nickname").value(MODIFY_NICKNAME))
                .andReturn();
        }
        @Nested
        @DisplayName("자신의 사용자 정보 수정하기 실패")
        class UpdateMyInfoFail{
            @Test
            @DisplayName("유효하지 않은 액세스 토큰의 경우, 'INVALID_ACCESS_TOKEN' 예외를 반환한다")
            void When_RequestInvalidAccessToken_Expect_ThrowException(){
                // given
                final UpdateMyInfoRequest request = UpdateMyInfoRequest.builder()
                    .password(PW)
                    .nickname(MODIFY_NICKNAME)
                    .build();

                doThrow(new RestApiException(INVALID_ACCESS_TOKEN)).when(memberService).updateMyInfo(any(MemberEmailDto.class), any(UpdateMyInfoRequest.class));

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(INVALID_ACCESS_TOKEN));
            }
        }
    }
}
