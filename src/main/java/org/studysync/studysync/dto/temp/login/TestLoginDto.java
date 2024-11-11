package org.studysync.studysync.dto.temp.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TestLoginDto {
    private TestLoginToken token;

    public static TestLoginDto of(String accessToken, String refreshToken) {
        return TestLoginDto.builder()
                .token(TestLoginToken.of(accessToken, refreshToken))
                .build();
    }
}
