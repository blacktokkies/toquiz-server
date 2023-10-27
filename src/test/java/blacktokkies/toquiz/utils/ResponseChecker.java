package blacktokkies.toquiz.utils;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ResponseChecker {
    public static void checkResultIsAuthenticationResponse(ResultActions resultActions) throws Exception {
        resultActions.andExpect(jsonPath("$.result.id").exists())
            .andExpect(jsonPath("$.result.email").exists())
            .andExpect(jsonPath("$.result.nickname").exists())
            .andExpect(jsonPath("$.result.provider").exists())
            .andExpect(jsonPath("$.result.createdAt").exists())
            .andExpect(jsonPath("$.result.updatedAt").exists())
            .andExpect(jsonPath("$.result.accessToken").exists())
            .andReturn();
    }
}
