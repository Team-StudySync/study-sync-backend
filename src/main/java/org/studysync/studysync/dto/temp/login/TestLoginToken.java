package org.studysync.studysync.dto.temp.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class TestLoginToken {
    @Schema(description = "서비스 accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIy...")
    private String accessToken;
    @Schema(description = "서비스 refreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3...")
    private String refreshToken;

    public static TestLoginToken of(String accessToken, String refreshToken) {
        return TestLoginToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
