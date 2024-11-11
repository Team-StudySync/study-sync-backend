package org.studysync.studysync.dto.invitation.Invitation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InvitationResponseDto {
    @Schema(example = "1", description = "그룹 아이디")
    private String groupId;

    @Schema(example = "1", description = "초대 코드")
    private String code;

    public static InvitationResponseDto fromDto(InvitationDto invitationDto) {
        return InvitationResponseDto.builder()
                .groupId(invitationDto.getGroupId())
                .code(invitationDto.getCode())
                .build();
    }
}
