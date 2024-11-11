package org.studysync.studysync.dto.temp.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "LoginResponse")
public class TestLoginResponseDto {
    @Schema(description = "응답 메시지", example = "성공적으로 로그인 하였습니다.")
    private String message;
    @Schema(description = "서비스 토큰 정보")
    private TestLoginToken token;

    public static TestLoginResponseDto fromDto(TestLoginDto dto) {
        return TestLoginResponseDto.builder()
                .message("성공적으로 로그인 하였습니다.")
                .token(dto.getToken())
                .build();
    }
}
