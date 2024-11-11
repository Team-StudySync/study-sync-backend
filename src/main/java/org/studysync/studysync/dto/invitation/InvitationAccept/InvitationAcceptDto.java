package org.studysync.studysync.dto.invitation.InvitationAccept;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.domain.Member;
import org.studysync.studysync.dto.common.StudyGroupInfo;

@Getter
@AllArgsConstructor
@Builder
public class InvitationAcceptDto {
    private Long memberId;
    private StudyGroupInfo studyGroupInfo;

    public static InvitationAcceptDto fromEntity(Member member){
        return InvitationAcceptDto.builder()
                .memberId(member.getId())
                .studyGroupInfo(StudyGroupInfo.fromEntity(member.getGroup()))
                .build();
    }
}
