package org.studysync.studysync.dto.invitation.InvitationAccept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.domain.Member;
import org.studysync.studysync.dto.common.StudyGroupInfo;

@Getter
@AllArgsConstructor
@Builder
public class InvitationAcceptResponseDto {
    @Schema(example = "1", description = "멤버 아이디")
    private Long memberId;

    private StudyGroupInfo studyGroupInfo;

    public static InvitationAcceptResponseDto fromDto(InvitationAcceptDto dto){
        return InvitationAcceptResponseDto.builder()
                .memberId(dto.getMemberId())
                .studyGroupInfo(dto.getStudyGroupInfo())
                .build();
    }
}
