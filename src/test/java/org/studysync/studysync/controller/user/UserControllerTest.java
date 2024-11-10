package org.studysync.studysync.controller.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.constant.SnsType;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.user.UserInfoDto;
import org.studysync.studysync.exception.HttpErrorException;
import org.studysync.studysync.service.user.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("회원 정보 조회")
    class UserInfoGet {

        private static User user;

        @BeforeAll
        static void setUp() {
            user = new User(1L, "snsId", SnsType.Kakao, "최민수", "profileImage", "email", "");
        }

        @Test
        @WithMockUser
        @DisplayName("성공 응답")
        void requestToGetUserInfo_ResponseSuccess() throws Exception {
            // given
            UserInfoDto userInfoDto = UserInfoDto.fromEntity(user);

            given(userService.getUserInfo(any())).willReturn(userInfoDto);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/user")
                    .with(csrf()));

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.snsId").value("snsId"))
                    .andExpect(jsonPath("$.snsType").value("Kakao"))
                    .andExpect(jsonPath("$.email").value("email"))
                    .andExpect(jsonPath("$.profileImage").value("profileImage"))
                    .andExpect(jsonPath("$.name").value("최민수"));
        }

        @Test
        @WithMockUser
        @DisplayName("AccessToken 없이 요청할 경우 에러 응답")
        void requestWithoutAccessToken_ResponseError() throws Exception {
            // given
            given(userService.getUserInfo(any())).willThrow(new HttpErrorException(HttpErrorCode.AccessDeniedError));

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/user")
                    .with(csrf()));

            // then
            resultActions
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.AccessDeniedError.name()))
                    .andExpect(jsonPath("$.message").value(HttpErrorCode.AccessDeniedError.getMessage()));
        }

        @Test
        @WithMockUser
        @DisplayName("유효하지 않은 AccessToken으로 요청할 경우 에러 응답")
        void requestWithNotValidAccessToken_ResponseError() throws Exception {
            // given
            given(userService.getUserInfo(any())).willThrow(new HttpErrorException(HttpErrorCode.NotValidAccessTokenError));

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/user")
                    .with(csrf()));

            // then
            resultActions
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.NotValidAccessTokenError.name()))
                    .andExpect(jsonPath("$.message").value(HttpErrorCode.NotValidAccessTokenError.getMessage()));
        }

        @Test
        @WithMockUser
        @DisplayName("만료된 AccessToken으로 요청할 경우 에러 응답")
        void requestWithExpiredAccessTokenError_ResponseError() throws Exception {
            // given
            given(userService.getUserInfo(any())).willThrow(new HttpErrorException(HttpErrorCode.ExpiredAccessTokenError));

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/user")
                    .with(csrf()));

            // then
            resultActions
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.ExpiredAccessTokenError.name()))
                    .andExpect(jsonPath("$.message").value(HttpErrorCode.ExpiredAccessTokenError.getMessage()));
        }

        @Test
        @WithMockUser
        @DisplayName("올바르지 않은 Payload를 가진 AccessToken으로 요청할 경우 에러 응답")
        void requestWithNotValidAccessTokenWithoutClaim_ResponseError() throws Exception {
            // given
            given(userService.getUserInfo(any())).willThrow(new HttpErrorException(HttpErrorCode.UserNotFoundError));

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/user")
                    .with(csrf()));

            // then
            resultActions
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value(HttpErrorCode.UserNotFoundError.name()))
                    .andExpect(jsonPath("$.message").value(HttpErrorCode.UserNotFoundError.getMessage()));
        }
    }
}