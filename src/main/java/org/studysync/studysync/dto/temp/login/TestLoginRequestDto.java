package org.studysync.studysync.dto.temp.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.studysync.studysync.constant.SnsType;

@Getter
@Setter
public class TestLoginRequestDto {
    @Schema(example = "1", description = "테스트 유저 아이디")
    @NotNull(message = "테스트 유저 아이디가 필요합니다.")
    private Long testUserId;
}
