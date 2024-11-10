package org.studysync.studysync.service.auth;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.dto.auth.oauth.kakao.KakaoUserInfoResponse;
import org.studysync.studysync.exception.HttpErrorException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


class KakaoOAuthServiceTest {
    private static KakaoOAuthService kakaoOAuthService;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        final String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        final WebClient webClient = WebClient.create(baseUrl);
        kakaoOAuthService = new KakaoOAuthService(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    @DisplayName("카카오 유저 정보 조회")
    class KakaoUserInfo {
        @Test
        @DisplayName("성공")
        void validKakaoAccessToken_ReturnKakaoUserInfo() throws IOException {
            String accessToken = "testAccessToken";

            String responseJson = "{\n" +
                    "  \"id\": 123,\n" +
                    "  \"connected_at\": 123,\n" +
                    "  \"properties\": {\n" +
                    "    \"nickname\": \"최민수\",\n" +
                    "    \"profile_image\": \"http://k.kakaocdn.net/a.jpg\",\n" +
                    "    \"thumbnail_image\": \"http://k.kakaocdn.net/a.jpg\"\n" +
                    "  },\n" +
                    "  \"kakao_account\": {\n" +
                    "    \"profile\": {\n" +
                    "      \"nickname\": \"최민수\",\n" +
                    "      \"thumbnail_image_url\": \"http://k.kakaocdn.net/a.jpg\",\n" +
                    "      \"profile_image_url\": \"http://k.kakaocdn.net/a.jpg\",\n" +
                    "      \"is_default_image\": false,\n" +
                    "      \"is_default_nickname\": false\n" +
                    "    },\n" +
                    "    \"profile_needs_agreement\": false,\n" +
                    "    \"email_needs_agreement\": false\n" +
                    "  }\n" +
                    "}";
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(responseJson)
                    .addHeader("Content-Type", "application/json"));

            KakaoUserInfoResponse userInfo = kakaoOAuthService.getUserInfo(accessToken);
            Assertions.assertThat(userInfo.getId()).isEqualTo(123);
            Assertions.assertThat(userInfo.getKakaoAccount().getProfile().getNickname()).isEqualTo("최민수");
        }

        @Test
        @DisplayName("Unauthorized Token일 경우 에러 반환")
        void unauthorizedKakaoAccessToken_ReturnError() {
            String accessToken = "invalidAccessToken";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(401)
                    .addHeader("Content-Type", "application/json"));

            assertThatThrownBy(() -> kakaoOAuthService.getUserInfo(accessToken))
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessageContaining(HttpErrorCode.UnauthorizedKakaoError.getMessage());
        }

        @Test
        @DisplayName("Forbidden Token일 경우 에러 반환")
        void forbiddenKakaoAccessToken_ReturnError() {
            String accessToken = "forbiddenAccessToken";

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(403)
                    .addHeader("Content-Type", "application/json"));

            assertThatThrownBy(() -> kakaoOAuthService.getUserInfo(accessToken))
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessageContaining(HttpErrorCode.ForbiddenKakaoError.getMessage());
        }
    }
}