package blacktokkies.toquiz.member.api;

import blacktokkies.toquiz.domain.member.api.MemberApi;
import blacktokkies.toquiz.domain.member.application.MemberService;
import blacktokkies.toquiz.domain.member.dto.request.UpdateMyInfoRequest;
import blacktokkies.toquiz.domain.member.dto.response.MemberInfoResponse;
import blacktokkies.toquiz.domain.model.Provider;
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

import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.INVALID_ACCESS_TOKEN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberApiTest {
    @InjectMocks
    private MemberApi memberApi;

    @Mock
    private MemberService memberService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    private final String accessToken = "eyJhbGciOiJIUzI1NiJ9" +
        ".eyJzdWIiOiJzb29tYW4zMTFAbmF2ZXIuY29tIiwiaWF0IjoxNjkzMjMyMDMyLCJleHAiOjE2OTM0MDQ4MzJ9.TiElWh1AX3T7Rb_Q2g4izZXMiVy_kpFiacEkNnhpNdE";

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
                    .header("Authorization", accessToken)
            );
        }

        @Test
        void 자신의_사용자_정보_가져오기() throws Exception {
            // given
            final MemberInfoResponse response = MemberInfoResponse.builder()
                .id(1L)
                .email("test311@naver.com")
                .provider(Provider.LOCAL)
                .nickname("TEST")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            doReturn(response).when(memberService).getMyInfo();

            // when
            final ResultActions resultActions = requestApi();

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(new SuccessResponse<>(response).getStatusCode()))
                .andReturn();
        }

        @Nested
        class 자신의_사용자_정보_가져오기_실패{
            @Test
            void 유효하지_않은_액세스_토큰(){
                // given
                doThrow(new RestApiException(INVALID_ACCESS_TOKEN)).when(memberService).getMyInfo();

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
                    .header("Authorization", accessToken)
            );
        }
        @Test
        void 자신의_사용자_정보_수정하기_성공() throws Exception {
            // given
            final UpdateMyInfoRequest request = UpdateMyInfoRequest.builder()
                .password("modify@311")
                .nickname("modify")
                .build();

            final MemberInfoResponse response = MemberInfoResponse.builder()
                .id(1L)
                .email("test311@naver.com")
                .provider(Provider.LOCAL)
                .nickname("modify")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            doReturn(response).when(memberService).updateMyInfo(any(UpdateMyInfoRequest.class));

            // when
            final ResultActions resultActions = requestApi(request);

            // then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(new SuccessResponse<>(response).getStatusCode()))
                .andReturn();
        }
        @Nested
        class 자신의_사용자_정보_수정하기_실패{
            @Test
            void 유효하지_않은_액세스_토큰(){
                // given
                final UpdateMyInfoRequest request = UpdateMyInfoRequest.builder()
                    .password("modify@311")
                    .nickname("modify")
                    .build();

                doThrow(new RestApiException(INVALID_ACCESS_TOKEN)).when(memberService).updateMyInfo(any(UpdateMyInfoRequest.class));

                // when, then
                assertThatThrownBy(() -> requestApi(request)).hasCause(new RestApiException(INVALID_ACCESS_TOKEN));
            }
        }
    }
}
