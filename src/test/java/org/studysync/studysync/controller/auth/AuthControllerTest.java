package org.studysync.studysync.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.constant.SnsType;
import org.studysync.studysync.dto.auth.login.LoginDto;
import org.studysync.studysync.dto.auth.login.LoginRequestDto;
import org.studysync.studysync.dto.auth.tokenReissue.TokenReIssueDto;
import org.studysync.studysync.service.auth.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("로그인")
    class Login {
        @Test
        @WithMockUser
        @DisplayName("성공 응답")
        void validOAuthAccessToken_ResponseSuccess() throws Exception {
            // given
            LoginRequestDto requestDto = LoginRequestDto.builder()
                    .accessToken("kakaoAccessToken")
                    .snsType(SnsType.Kakao)
                    .build();
            requestDto.setAccessToken("testSnsAccessToken");
            requestDto.setSnsType(SnsType.Kakao);
            String requestBody = objectMapper.writeValueAsString(requestDto);

            LoginDto dto = LoginDto.of("testAccessToken", "testRefreshToken");

            given(authService.login(any())).willReturn(dto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .with(csrf()));

            // then
            resultActions
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.message").value("성공적으로 로그인 하였습니다."))
                    .andExpect(jsonPath("$.token.accessToken").value(dto.getToken().getAccessToken()))
                    .andExpect(jsonPath("$.token.refreshToken").value(dto.getToken().getRefreshToken()))
                    .andDo(print());

            verify(authService, times(1)).login(any());
        }

        @Test
        @WithMockUser
        @DisplayName("accessToken 없이 요청 할 경우 에러를 응답")
        void noAccessToken_ResponseError() throws Exception {
            LoginRequestDto requestDto = LoginRequestDto.builder()
                    .accessToken(null)
                    .snsType(SnsType.Kakao)
                    .build();
            requestDto.setAccessToken(null);
            requestDto.setSnsType(SnsType.Kakao);
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .with(csrf()));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.NotValidRequestError.name()))
                    .andExpect(jsonPath("$.message").value(HttpErrorCode.NotValidRequestError.getMessage()))
                    .andExpect(jsonPath("$.errorDescriptions[0].message").value("accessToken이 필요합니다."))
                    .andDo(print());
            verify(authService, times(0)).login(any());
        }

        @Test
        @WithMockUser
        @DisplayName("snsType 없이 요청 할 경우 에러를 응답")
        void noSnsType_ResponseError() throws Exception {
            LoginRequestDto requestDto = LoginRequestDto.builder()
                    .accessToken("kakaoAccessToken")
                    .snsType(null)
                    .build();
            requestDto.setAccessToken("testSnsAccessToken");
            requestDto.setSnsType(null);
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .with(csrf()));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.NotValidRequestError.name()))
                    .andExpect(jsonPath("$.message").value(HttpErrorCode.NotValidRequestError.getMessage()))
                    .andExpect(jsonPath("$.errorDescriptions[0].message").value("소셜 계정의 타입이 필요합니다."))
                    .andDo(print());

            verify(authService, times(0)).login(any());
        }
    }


    @Nested
    @DisplayName("로그아웃")
    class Logout {
        @Test
        @WithMockUser
        @DisplayName("성공 응답")
        void validRefreshToken_ResponseSuccess() throws Exception {
            //given
            String refreshToken = "testRefreshToken";
            
            //when
            ResultActions resultActions = mockMvc.perform(post("/api/auth/logout")
                    .header("Authorization-refresh", refreshToken)
                    .with(csrf()));

            resultActions.andExpect(status().isCreated());

            verify(authService, times(1)).logout(any());
        }

        @Test
        @WithMockUser
        @DisplayName("refreshToken 없이 요청 할 경우 에러를 응답")
        void noRefreshToken_ResponseError() throws Exception {
            HttpErrorCode missingRequestHeaderError = HttpErrorCode.MissingRequestHeaderError;

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/auth/logout")
                    .with(csrf()));

            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(missingRequestHeaderError.name()))
                    .andExpect(jsonPath("$.message").value(missingRequestHeaderError.getMessage()))
                    .andDo(print());

            verify(authService, times(0)).logout(any());
        }
    }

    @Nested
    @DisplayName("토큰 재발급")
    class tokenReissue{
        @Test
        @WithMockUser
        @DisplayName("성공 응답")
        void validAccessTokenAndRefeshToken_ResponseSuccess() throws Exception {
            String accessToken = "testAccessToken";
            String refreshToken = "testRefreshToken";

            TokenReIssueDto dto = TokenReIssueDto.of("reIssuedAccessToken");

            given(authService.reIssueToken(any(), any())).willReturn(dto);

            ResultActions resultActions = mockMvc.perform(post("/api/auth/refreshToken")
                    .header("Authorization-refresh", refreshToken)
                    .header("Authorization", accessToken)
                    .with(csrf()));

            resultActions
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.message").value("토큰이 재발행되었습니다"))
                    .andExpect(jsonPath("$.accessToken").value(dto.getAccessToken()));
        }

        @Test
        @WithMockUser
        @DisplayName("accessToken 없이 요청 할 경우 에러를 응답")
        void noAccessToken_ResponseError() throws Exception {
            HttpErrorCode missingRequestHeaderError = HttpErrorCode.MissingRequestHeaderError;
            String accessToken = "testAccessToken";

            TokenReIssueDto dto = TokenReIssueDto.of("reIssuedAccessToken");

            given(authService.reIssueToken(any(), any())).willReturn(dto);

            ResultActions resultActions = mockMvc.perform(post("/api/auth/refreshToken")
                    .header("Authorization", accessToken)
                    .with(csrf()));

            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(missingRequestHeaderError.name()))
                    .andExpect(jsonPath("$.message").value(missingRequestHeaderError.getMessage()))
                    .andDo(print());
        }

        @Test
        @WithMockUser
        @DisplayName("refreshToken 없이 요청 할 경우 에러를 응답")
        void noRefreshToken_ResponseError() throws Exception {
            HttpErrorCode missingRequestHeaderError = HttpErrorCode.MissingRequestHeaderError;
            String refreshToken = "testRefreshToken";

            TokenReIssueDto dto = TokenReIssueDto.of("reIssuedAccessToken");

            given(authService.reIssueToken(any(), any())).willReturn(dto);

            ResultActions resultActions = mockMvc.perform(post("/api/auth/refreshToken")
                    .header("Authorization-refresh", refreshToken)
                    .with(csrf()));

            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(missingRequestHeaderError.name()))
                    .andExpect(jsonPath("$.message").value(missingRequestHeaderError.getMessage()))
                    .andDo(print());
        }
    }
}