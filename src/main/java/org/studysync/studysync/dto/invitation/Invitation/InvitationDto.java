package org.studysync.studysync.dto.invitation.Invitation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InvitationDto {
    private String groupId;
    private String code;

    public static InvitationDto of(String groupId, String code) {
        return InvitationDto.builder()
                .groupId(groupId)
                .code(code)
                .build();
    }
}
