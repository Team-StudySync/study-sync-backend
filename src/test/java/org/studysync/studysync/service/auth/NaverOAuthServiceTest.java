package org.studysync.studysync.service.auth;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.dto.auth.oauth.naver.NaverUserInfoResponse;
import org.studysync.studysync.exception.HttpErrorException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NaverOAuthServiceTest {
    private static NaverOAuthService naverOAuthService;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        final WebClient webClient = WebClient.create(baseUrl);
        naverOAuthService = new NaverOAuthService(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }


    @Nested
    @DisplayName("네이버 유저정보 불러오기")
    class KakaoUserInfoTest{
        @Test
        @DisplayName("성공")
        void validNaverAccessToken_ReturnKakaoUserInfo(){
            String accessToken = "testAccessToken";

            String responseJson = "{\n" +
                    "  \"resultcode\": \"00\",\n" +
                    "  \"message\": \"success\",\n" +
                    "  \"response\": {\n" +
                    "    \"id\": \"21pKKKAyTN0-w_ZN\",\n" +
                    "    \"nickname\": null,\n" +
                    "    \"name\": \"최민수\",\n" +
                    "    \"email\": \"test01@naver.com\",\n" +
                    "    \"gender\": null,\n" +
                    "    \"age\": null,\n" +
                    "    \"birthday\": null,\n" +
                    "    \"profile_image\": \"https://ssl.pstatic.net/static/img.png\",\n" +
                    "    \"birthyear\": null,\n" +
                    "    \"mobile\": null\n" +
                    "  }\n" +
                    "}";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(responseJson)
                    .addHeader("Content-Type", "application/json"));

            NaverUserInfoResponse userInfo = naverOAuthService.getUserInfo(accessToken);
            Assertions.assertThat(userInfo.getMessage()).isEqualTo("success");
            Assertions.assertThat(userInfo.getResponseDetails().getName()).isEqualTo("최민수");
        }

        @Test
        @DisplayName("Unauthorized Token일 경우 에러 반환")
        void unauthorizedNaverAccessToken_ReturnError() {
            String accessToken = "invalidAccessToken";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(401)
                    .addHeader("Content-Type", "application/json"));

            assertThatThrownBy(() -> naverOAuthService.getUserInfo(accessToken))
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessageContaining(HttpErrorCode.UnauthorizedNaverError.getMessage());
        }

        @Test
        @DisplayName("Forbidden Token일 경우 에러 반환")
        void forbiddenNaverAccessToken_ReturnError() {
            String accessToken = "forbiddenAccessToken";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(403)
                    .addHeader("Content-Type", "application/json"));

            assertThatThrownBy(() -> naverOAuthService.getUserInfo(accessToken))
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessageContaining(HttpErrorCode.ForbiddenNaverError.getMessage());
        }
    }
}